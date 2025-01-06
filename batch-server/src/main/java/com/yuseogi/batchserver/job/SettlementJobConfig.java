package com.yuseogi.batchserver.job;

import com.yuseogi.batchserver.dao.SettlementDao;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class SettlementJobConfig {

    private static final int chunkSize = 200;

    private final DataSource dataSource;

    @Bean
    public Job settlementJob(
        JobRepository jobRepository,
        Step createSettlementStep
    ) {
        return new JobBuilder("settlementJob", jobRepository)
            .start(createSettlementStep)
            .build();
    }

    @Bean
    public Step createSettlementStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("createSettlementStep", jobRepository)
            .<SettlementDao, SettlementDao>chunk(chunkSize, transactionManager)
            .reader(settlementReader(null, null, null))
            .writer(settlementWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<SettlementDao> settlementReader(
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
            .name("settlementReader")
            .build();
    }

    @Bean
    public PagingQueryProvider settlementQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("""
            s.id                AS store_id,
            SUM(t.trade_amount) AS revenue,
            SUM(p.card_fee)     AS fee
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
        queryProvider.setGroupClause("store_id");

        queryProvider.setSortKey("store_id");

        return queryProvider.getObject();
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
