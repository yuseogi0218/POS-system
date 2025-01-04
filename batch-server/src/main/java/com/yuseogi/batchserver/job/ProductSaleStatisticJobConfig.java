package com.yuseogi.batchserver.job;

import com.yuseogi.batchserver.dao.ProductSaleStatisticDao;
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
public class ProductSaleStatisticJobConfig {

    private static final int chunkSize = 200;

    private final DataSource dataSource;

    @Bean
    public Job productSaleStatisticJob(
        JobRepository jobRepository,
        Step createProductSaleStatisticStep
    ) {
        return new JobBuilder("productSaleCountStatisticJob", jobRepository)
            .start(createProductSaleStatisticStep)
            .build();
    }

    @Bean
    public Step createProductSaleStatisticStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("createProductSaleStatisticStep", jobRepository)
            .<ProductSaleStatisticDao, ProductSaleStatisticDao>chunk(chunkSize, transactionManager)
            .reader(productSaleStatisticReader(null, null, null))
            .writer(productSaleStatisticWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<ProductSaleStatisticDao> productSaleStatisticReader(
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
            .queryProvider(productSaleStatisticQueryProvider())
            .parameterValues(parameterValues)
            .name("productSaleStatisticReader")
            .build();
    }

    @Bean
    public PagingQueryProvider productSaleStatisticQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("product_id, sale_count, sale_amount");
        queryProvider.setFromClause("""
            FROM (
                SELECT
                    p.id                                                                                   AS product_id,
                    ROW_NUMBER() OVER (PARTITION BY p.store_id ORDER BY SUM(od.count) DESC, p.name)        AS count_ranking,
                    ROW_NUMBER() OVER (PARTITION BY p.store_id ORDER BY SUM(od.total_amount) DESC, p.name) AS amount_ranking,
                    SUM(od.count)                                                                          AS sale_count,
                    SUM(od.total_amount)                                                                   AS sale_amount
                FROM product p
                    JOIN order_detail od ON od.product_id = p.id
                WHERE :startDate <= DATE(od.created_at) AND DATE(od.created_at) < :endDate
                GROUP BY p.store_id, p.id
            ) AS ranked_products
        """);
        queryProvider.setWhereClause("count_ranking <= 5 OR amount_ranking <= 5");

        queryProvider.setSortKey("product_id");

        return queryProvider.getObject();
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
