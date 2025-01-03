package com.yuseogi.batchserver.job;

import com.yuseogi.batchserver.dao.ProductSaleStatisticItemDao;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class ProductSaleStatisticJobConfig {

    private static final int chunkSize = 10;

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public Job productSaleStatisticJob(
        JobRepository jobRepository,
        Step createProductSaleStatisticStep,
        Step createProductSaleStatisticItemStep
    ) {
        return new JobBuilder("productSaleCountStatisticJob", jobRepository)
            .start(createProductSaleStatisticStep)
            .next(createProductSaleStatisticItemStep)
            .build();
    }

    @Bean
    public Step createProductSaleStatisticStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("createProductSaleStatisticStep", jobRepository)
            .tasklet(createProductSaleStatisticTasklet(null, null, null, null), transactionManager)
            .listener(promotionListener())
        .build();
    }

    @Bean
    @StepScope
    public Tasklet createProductSaleStatisticTasklet(
        @Value("#{jobParameters[storeId]}") Long storeId,
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate,
        @Value("#{jobParameters[productCategory]}") String productCategory
    ) {
        return (contribution, chunkContext) -> {

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO product_sale_statistic (store_id, date_term, start_date, product_category) VALUE(?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS
                    );
                    ps.setLong(1, storeId);
                    ps.setString(2, dateTerm);
                    ps.setDate(3, Date.valueOf(startDate));
                    ps.setString(4, productCategory);

                    return ps;
                }, keyHolder);

            Long generatedProductSaleStatisticId = keyHolder.getKey().longValue();

            ExecutionContext executionContext = chunkContext.getStepContext()
                .getStepExecution()
                .getExecutionContext();
            executionContext.put("statisticId", generatedProductSaleStatisticId);

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[] {"statisticId"});
        return listener;
    }

    @Bean
    public Step createProductSaleStatisticItemStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("productSaleCountStatisticStep", jobRepository)
            .<ProductSaleStatisticItemDao, ProductSaleStatisticItemDao>chunk(chunkSize, transactionManager)
            .reader(productSaleStatisticItemReader(null, null, null, null, null))
            .writer(productSaleStatisticItemWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<ProductSaleStatisticItemDao> productSaleStatisticItemReader(
        @Value("#{jobExecutionContext[statisticId]}") Long statisticId,
        @Value("#{jobParameters[storeId]}") Long storeId,
        @Value("#{jobParameters[startDate]}") LocalDate startDate,
        @Value("#{jobParameters[endDate]}") LocalDate endDate,
        @Value("#{jobParameters[productCategory]}") String productCategory
    ) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("storeId", storeId);
        parameterValues.put("startDate", startDate);
        parameterValues.put("endDate", endDate);
        parameterValues.put("productCategory", productCategory);

        return new JdbcPagingItemReaderBuilder<ProductSaleStatisticItemDao>()
            .pageSize(chunkSize)
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper((rs, rowNum) -> new ProductSaleStatisticItemDao(
                statisticId,
                rs.getInt("count_ranking"),
                rs.getInt("amount_ranking"),
                rs.getString("product_name"),
                rs.getInt("product_price"),
                rs.getInt("sale_count"),
                rs.getInt("sale_amount")
            ))
            .queryProvider(queryProvider())
            .parameterValues(parameterValues)
            .name("productSaleStatisticItemReader")
            .build();
    }

    @Bean
    public PagingQueryProvider queryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("count_ranking, amount_ranking, product_name, product_price, sale_count, sale_amount");
        queryProvider.setFromClause("""
            FROM (
                SELECT
                    ROW_NUMBER() OVER (ORDER BY SUM(od.count) DESC, od.product_name ASC) AS count_ranking,
                    ROW_NUMBER() OVER (ORDER BY SUM(od.total_amount) DESC, od.product_name ASC) AS amount_ranking,
                    od.product_name,
                    od.product_price,
                    SUM(od.count) AS sale_count,
                    SUM(od.total_amount) AS sale_amount
                FROM
                    trade t
                JOIN order_table o ON o.trade_id = t.id
                JOIN order_detail od ON od.order_id = o.id
                WHERE
                    t.store_id = :storeId AND
                    t.is_completed = 'Y' AND
                    :startDate <= DATE(t.created_at) AND DATE(t.created_at) < :endDate AND
                    od.product_category = :productCategory
                GROUP BY
                    od.product_name, od.product_price
            ) AS ranked_products
        """);
        queryProvider.setWhereClause("count_ranking <= 5 OR amount_ranking <= 5");

        //TODO: 2025-01-3 Sort Key 고유한 값으로 수정 필요 또는 안쓸수 있는 방법 찾아보기
        queryProvider.setSortKey("count_ranking");

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<ProductSaleStatisticItemDao> productSaleStatisticItemWriter() {
        return new JdbcBatchItemWriterBuilder<ProductSaleStatisticItemDao>()
            .dataSource(dataSource)
            .sql("INSERT INTO product_sale_statistic_item (statistic_id, count_ranking, amount_ranking, product_name, product_price, sale_count, sale_amount)" +
                "VALUES (:statisticId, :countRanking, :amountRanking, :productName, :productPrice, :saleCount, :saleAmount)")
            .beanMapped()
            .build();
    }

}
