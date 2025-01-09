package com.yuseogi.batchserver.job;

import com.yuseogi.batchserver.ItemReader.RedisItemReader;
import com.yuseogi.batchserver.dao.ProductSaleStatisticDao;
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
public class ProductSaleStatisticJobConfig {

    private static final int chunkSize = 1000;

    private final DataSource dataSource;

    private final RedisTemplate<String, Object> redisTemplate;

    @Bean
    public Job productSaleStatisticJob(
        JobRepository jobRepository,
        Step createProductSaleStatisticStep,
        Step transferProductSaleStatisticFromRedisToDatabaseStep
    ) {
        return new JobBuilder("productSaleStatisticJob", jobRepository)
            .start(createProductSaleStatisticStep)
            .next(transferProductSaleStatisticFromRedisToDatabaseStep)
            .build();
    }

    @Bean
    public Step createProductSaleStatisticStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("createProductSaleStatisticStep", jobRepository)
            .<ProductSaleStatisticDao, ProductSaleStatisticDao>chunk(chunkSize, transactionManager)
            .reader(productSaleItemReader(null, null, null))
            .writer(productSaleItemWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<ProductSaleStatisticDao> productSaleItemReader(
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate,
        @Value("#{jobParameters[endDate]}") LocalDate endDate
    ) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("startDate", startDate);
        parameterValues.put("endDate", endDate);

        return new JdbcPagingItemReaderBuilder<ProductSaleStatisticDao>()
            .pageSize(chunkSize)
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper((rs, rowNum) -> new ProductSaleStatisticDao(
                rs.getLong("product_id"),
                dateTerm,
                startDate,
                rs.getInt("sale_count"),
                rs.getInt("sale_amount")
            ))
            .queryProvider(productSaleItemQueryProvider())
            .parameterValues(parameterValues)
            .name("productSaleItemReader")
            .build();
    }

    @Bean
    public PagingQueryProvider productSaleItemQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("""
            od.id,
            p.id AS product_id,
            od.count AS sale_count,
            od.total_amount AS sale_amount
        """);
        queryProvider.setFromClause("""
            From product p
                JOIN order_detail od ON od.product_id = p.id
        """);
        queryProvider.setWhereClause("""
            :startDate <= od.created_at AND od.created_at < :endDate
        """);
        queryProvider.setSortKey("od.id");

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<ProductSaleStatisticDao> productSaleItemWriter() {
        return items -> redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                HashOperations<K, String, Object> hashOperations = operations.opsForHash();

                for (ProductSaleStatisticDao item : items) {
                    Long productId = item.productId();
                    String key = "product_sale_statistic" + "-" + item.dateTerm() + "-" + productId;

                    hashOperations.put((K) key, "product_id", productId);
                    hashOperations.increment((K) key, "sale_count", item.saleCount());
                    hashOperations.increment((K) key, "sale_amount", item.saleAmount());
                }
                return null;
            }
        });
    }

    @Bean
    public Step transferProductSaleStatisticFromRedisToDatabaseStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("transferProductSaleStatisticFromRedisToDatabaseStep", jobRepository)
            .<Map<String, Object>, ProductSaleStatisticDao>chunk(chunkSize, transactionManager)
            .reader(productSaleStatisticReader(null))
            .processor(productSaleStatisticProcessor(null, null))
            .writer(productSaleStatisticWriter())
            .build();
    }

    @Bean
    @StepScope
    public RedisItemReader productSaleStatisticReader(
        @Value("#{jobParameters[dateTerm]}") String dateTerm
    ) {
        return new RedisItemReader(redisTemplate, "product_sale_statistic" + "-" + dateTerm + "-*");
    }

    @Bean
    @StepScope
    public ItemProcessor<Map<String, Object>, ProductSaleStatisticDao> productSaleStatisticProcessor(
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate
    ) {
        return item -> {
            Map<String, Object> values = item;

            return new ProductSaleStatisticDao(
                Long.valueOf(values.get("product_id").toString()),
                dateTerm,
                startDate,
                Integer.valueOf(values.get("sale_count").toString()),
                Integer.valueOf(values.get("sale_amount").toString())
            );
        };
    }

    @Bean
    public ItemWriter<ProductSaleStatisticDao> productSaleStatisticWriter() {
        return new JdbcBatchItemWriterBuilder<ProductSaleStatisticDao>()
            .dataSource(dataSource)
            .sql("INSERT INTO product_sale_statistic (product_id, date_term, start_date, sale_count, sale_amount)" +
                "VALUES (:productId, :dateTerm, :startDate, :saleCount, :saleAmount)")
            .beanMapped()
            .build();
    }

}
