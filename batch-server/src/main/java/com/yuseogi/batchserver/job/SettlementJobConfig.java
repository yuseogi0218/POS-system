package com.yuseogi.batchserver.job;

import com.yuseogi.batchserver.ItemReader.RedisItemReader;
import com.yuseogi.batchserver.dao.SettlementDao;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class SettlementJobConfig {

    private static final int chunkSize = 1000;

    private final DataSource dataSource;

    private final RedisTemplate<String, Object> redisTemplate;

    @Bean
    public Job settlementJob(
        JobRepository jobRepository,
        Step createSettlementStep,
        Step transferSettlementFromRedisToDatabaseStep
    ) {
        return new JobBuilder("settlementJob", jobRepository)
            .start(createSettlementStep)
            .next(transferSettlementFromRedisToDatabaseStep)
            .build();
    }

    @Bean
    public Step createSettlementStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("createSettlementStep", jobRepository)
            .<SettlementDao, SettlementDao>chunk(chunkSize, transactionManager)
            .reader(settlementItemReader(null, null, null))
            .writer(settlementItemWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<SettlementDao> settlementItemReader(
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate,
        @Value("#{jobParameters[endDate]}") LocalDate endDate
    ) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("dateTerm", dateTerm);
        parameterValues.put("startDate", startDate);
        parameterValues.put("endDate", endDate);

        return new JdbcPagingItemReaderBuilder<SettlementDao>()
            .pageSize(chunkSize)
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper((rs, rowNum) -> new SettlementDao(
                rs.getLong("store_id"),
                dateTerm,
                startDate,
                rs.getInt("revenue"),
                rs.getInt("fee"),
                rs.getInt("revenue") - rs.getInt("fee")
            ))
            .queryProvider(settlementQueryProvider())
            .parameterValues(parameterValues)
            .name("settlementItemReader")
            .build();
    }

    @Bean
    public PagingQueryProvider settlementQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("""
            p.id,
            s.id           AS store_id,
            t.trade_amount AS revenue,
            p.card_fee     AS fee
        """);
        queryProvider.setFromClause("""
            FROM payment p
                JOIN trade t ON p.trade_id = t.id
                JOIN store s ON s.id = t.store_id
        """);
        queryProvider.setWhereClause("""
            :startDate <= DATE(p.created_at) AND DATE(p.created_at) < :endDate
            AND (:dateTerm = 'DAY' OR s.settlement_date = DAY(:startDate))
        """);

        queryProvider.setSortKey("p.id");

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<SettlementDao> settlementItemWriter() {
        return items -> redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                HashOperations<K, String, Object> hashOperations = operations.opsForHash();

                for (SettlementDao item : items) {
                    Long storeId = item.storeId();
                    String key = "settlement" + "-" + item.dateTerm() + "-" + storeId;

                    hashOperations.put((K) key, "store_id", storeId);
                    hashOperations.increment((K) key, "revenue", item.revenue());
                    hashOperations.increment((K) key, "fee", item.fee());
                }
                return null;
            }
        });
    }

    @Bean
    public Step transferSettlementFromRedisToDatabaseStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("transferSettlementRedisToDatabaseStep", jobRepository)
            .<Map<String, Object>, SettlementDao>chunk(chunkSize, transactionManager)
            .reader(settlementReader(null))
            .processor(settlementProcessor(null, null))
            .writer(settlementWriter())
            .build();
    }

    @Bean
    @StepScope
    public RedisItemReader settlementReader(
        @Value("#{jobParameters[dateTerm]}") String dateTerm
    ) {
        return new RedisItemReader(redisTemplate, "settlement" + "-" + dateTerm + "-*");
    }

    @Bean
    @StepScope
    public ItemProcessor<Map<String, Object>, SettlementDao> settlementProcessor(
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate
    ) {
        return item -> {
            Map<String, Object> values = item;
            Integer revenue = Integer.valueOf(values.get("revenue").toString());
            Integer fee = Integer.valueOf(values.get("fee").toString());

            return new SettlementDao(
                Long.valueOf(values.get("store_id").toString()),
                dateTerm,
                startDate,
                revenue,
                fee,
                revenue - fee
            );
        };
    }

    @Bean
    public ItemWriter<SettlementDao> settlementWriter() {
        return new JdbcBatchItemWriterBuilder<SettlementDao>()
            .dataSource(dataSource)
            .sql("INSERT INTO settlement (store_id, date_term, start_date, revenue, fee, operating_profit)" +
                "VALUES (:storeId, :dateTerm, :startDate, :revenue, :fee, :operatingProfit)")
            .beanMapped()
            .build();
    }
}
