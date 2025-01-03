package com.yuseogi.batchserver.job;

import com.yuseogi.batchserver.dao.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
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
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class AdjustmentConfig {

    private static final int chunkSize = 10;

    private static final String CARD_FEE_TYPE = "CARD";
    //TODO: 2025-01-03 1달 정산 시, 포스기 이용 요금 포함 할것
    private static final String POS_USAGE_FEE_TYPE = "POS_USAGE";

    private final DataSource dataSource;

    @Bean
    public Job adjustmentJob(
        JobRepository jobRepository,
        Step createAdjustmentStep,
        Step createRevenueAdjustmentStep,
        Step createRevenueAdjustmentItemStep,
        Step createCardFeeAdjustmentStep,
        Step createCardFeeAdjustmentItemStep
    ) {
        return new JobBuilder("adjustmentJob", jobRepository)
            .start(createAdjustmentStep)
            .next(createRevenueAdjustmentStep)
            .next(createRevenueAdjustmentItemStep)
            .next(createCardFeeAdjustmentStep)
            .next(createCardFeeAdjustmentItemStep)
            .build();
    }

    @Bean
    public Step createAdjustmentStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("createAdjustmentStep", jobRepository)
            .<AdjustmentDao, AdjustmentDao>chunk(chunkSize, transactionManager)
            .reader(adjustmentReader(null, null, null))
            .writer(adjustmentWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<AdjustmentDao> adjustmentReader(
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate,
        @Value("#{jobParameters[endDate]}") LocalDate endDate
    ) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("startDate", startDate);
        parameterValues.put("endDate", endDate);

        return new JdbcPagingItemReaderBuilder<AdjustmentDao>()
            .pageSize(chunkSize)
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper((rs, rowNum) -> new AdjustmentDao(
                rs.getLong("store_id"),
                dateTerm,
                startDate,
                rs.getInt("total_revenue"),
                rs.getInt("total_fee"),
                rs.getInt("operating_profit")
            ))
            .queryProvider(adjustmentReaderQueryProvider())
            .parameterValues(parameterValues)
            .name("adjustmentReader")
            .build();
    }

    @Bean
    public PagingQueryProvider adjustmentReaderQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);

        queryProvider.setSelectClause("""
            store_id,
            SUM(t.trade_amount) AS total_revenue,
            SUM(p.card_fee) AS total_fee,
            SUM(t.trade_amount) - SUM(p.card_fee) AS operating_profit  
        """);
        queryProvider.setFromClause("""
            FROM trade t
                JOIN payment p ON p.trade_id = t.id
        """);
        queryProvider.setWhereClause("""
            t.is_completed = 'Y' AND
            :startDate <= DATE(t.created_at) AND DATE(t.created_at) < :endDate    
        """);
        queryProvider.setGroupClause("store_id");
        queryProvider.setSortKey("store_id");

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<AdjustmentDao> adjustmentWriter() {
        return new JdbcBatchItemWriterBuilder<AdjustmentDao>()
            .dataSource(dataSource)
            .sql("INSERT INTO adjustment (store_id, date_term, start_date, total_revenue, total_fee, operating_profit)" +
                "VALUES (:storeId, :dateTerm, :startDate, :totalRevenue, :totalFee, :operatingProfit)")
            .beanMapped()
            .build();
    }

    @Bean
    public Step createRevenueAdjustmentStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("createRevenueAdjustmentStep", jobRepository)
            .<RevenueAdjustmentDao, RevenueAdjustmentDao>chunk(chunkSize, transactionManager)
            .reader(RevenueAdjustmentReader(null, null, null))
            .writer(RevenueAdjustmentWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<RevenueAdjustmentDao> RevenueAdjustmentReader(
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate,
        @Value("#{jobParameters[endDate]}") LocalDate endDate
    ) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("dateTerm", dateTerm);
        parameterValues.put("startDate", startDate);
        parameterValues.put("endDate", endDate);

        return new JdbcPagingItemReaderBuilder<RevenueAdjustmentDao>()
            .pageSize(chunkSize)
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper((rs, rowNum) -> new RevenueAdjustmentDao(
                rs.getLong("adjustment_id"),
                rs.getString("product_category"),
                rs.getInt("category_revenue_amount")
            ))
            .queryProvider(RevenueAdjustmentReaderQueryProvider())
            .parameterValues(parameterValues)
            .name("revenueAdjustmentReader")
            .build();
    }

    @Bean
    public PagingQueryProvider RevenueAdjustmentReaderQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);

        queryProvider.setSelectClause("""
            a.id AS adjustment_id,
            od.product_category AS product_category,
            SUM(od.total_amount) AS category_revenue_amount
        """);
        queryProvider.setFromClause("""
            FROM trade t
                JOIN store s ON s.id = t.store_id
                JOIN adjustment a ON a.store_id = s.id AND a.date_term = :dateTerm AND a.start_date = :startDate
                JOIN order_table ot ON ot.trade_id = t.id
                JOIN order_detail od ON od.order_id = ot.id
        """);
        queryProvider.setWhereClause("""
            t.is_completed = 'Y' AND
            :startDate <= DATE(t.created_at) AND DATE(t.created_at) < :endDate
        """);
        queryProvider.setGroupClause("adjustment_id, product_category");

        Map<String, Order> sortKeys = new LinkedHashMap<>();
        sortKeys.put("adjustment_id", Order.ASCENDING);
        sortKeys.put("product_category", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<RevenueAdjustmentDao> RevenueAdjustmentWriter() {
        return new JdbcBatchItemWriterBuilder<RevenueAdjustmentDao>()
            .dataSource(dataSource)
            .sql("INSERT INTO revenue_adjustment (adjustment_id, product_category, category_revenue_amount)" +
                "VALUES (:adjustmentId, :productCategory, :categoryRevenueAmount)")
            .beanMapped()
            .build();
    }

    @Bean
    public Step createRevenueAdjustmentItemStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("createRevenueAdjustmentItemStep", jobRepository)
            .<RevenueAdjustmentItemDao, RevenueAdjustmentItemDao>chunk(chunkSize, transactionManager)
            .reader(RevenueAdjustmentItemReader(null, null, null))
            .writer(RevenueAdjustmentItemWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<RevenueAdjustmentItemDao> RevenueAdjustmentItemReader(
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate,
        @Value("#{jobParameters[endDate]}") LocalDate endDate
    ) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("dateTerm", dateTerm);
        parameterValues.put("startDate", startDate);
        parameterValues.put("endDate", endDate);

        return new JdbcPagingItemReaderBuilder<RevenueAdjustmentItemDao>()
            .pageSize(chunkSize)
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper((rs, rowNum) -> new RevenueAdjustmentItemDao(
                rs.getLong("revenue_adjustment_id"),
                rs.getString("product_name"),
                rs.getInt("product_price"),
                rs.getInt("sale_count"),
                rs.getInt("sale_amount")
            ))
            .queryProvider(RevenueAdjustmentItemReaderQueryProvider())
            .parameterValues(parameterValues)
            .name("revenueAdjustmentItemReader")
            .build();
    }

    @Bean
    public PagingQueryProvider RevenueAdjustmentItemReaderQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);

        queryProvider.setSelectClause("""
            ra.id AS revenue_adjustment_id,
            od.product_name AS product_name,
            od.product_price AS product_price,
            SUM(od.count) AS sale_count,
            SUM(od.total_amount) AS sale_amount
        """);
        queryProvider.setFromClause("""
            FROM trade t
                JOIN store s ON s.id = t.store_id
                JOIN adjustment a ON a.store_id = s.id AND a.date_term = :dateTerm AND a.start_date = :startDate
                JOIN revenue_adjustment ra ON ra.adjustment_id = a.id
                JOIN order_table ot ON ot.trade_id = t.id
                JOIN order_detail od ON od.order_id = ot.id AND ra.product_category = od.product_category
        """);
        queryProvider.setWhereClause("""
            t.is_completed = 'Y' AND
            :startDate <= DATE(t.created_at) AND DATE(t.created_at) < :endDate
        """);
        queryProvider.setGroupClause("revenue_adjustment_id, product_name, product_price");

        Map<String, Order> sortKeys = new LinkedHashMap<>();
        sortKeys.put("revenue_adjustment_id", Order.ASCENDING);
        sortKeys.put("product_name", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<RevenueAdjustmentItemDao> RevenueAdjustmentItemWriter() {
        return new JdbcBatchItemWriterBuilder<RevenueAdjustmentItemDao>()
            .dataSource(dataSource)
            .sql("INSERT INTO revenue_adjustment_item (revenue_adjustment_id, product_name, product_price, sale_count, sale_amount)" +
                "VALUES (:revenueAdjustmentId, :productName, :productPrice, :saleCount, :saleAmount)")
            .beanMapped()
            .build();
    }

    @Bean
    public Step createCardFeeAdjustmentStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("createCardFeeAdjustmentStep", jobRepository)
            .<FeeAdjustmentDao, FeeAdjustmentDao>chunk(chunkSize, transactionManager)
            .reader(CardFeeAdjustmentReader(null, null, null))
            .writer(CardFeeAdjustmentWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<FeeAdjustmentDao> CardFeeAdjustmentReader(
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate,
        @Value("#{jobParameters[endDate]}") LocalDate endDate
    ) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("dateTerm", dateTerm);
        parameterValues.put("startDate", startDate);
        parameterValues.put("endDate", endDate);

        return new JdbcPagingItemReaderBuilder<FeeAdjustmentDao>()
            .pageSize(chunkSize)
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper((rs, rowNum) -> new FeeAdjustmentDao(
                rs.getLong("adjustment_id"),
                CARD_FEE_TYPE,
                rs.getInt("category_fee_amount")
            ))
            .queryProvider(CardFeeAdjustmentReaderQueryProvider())
            .parameterValues(parameterValues)
            .name("cardFeeAdjustmentReader")
            .build();
    }

    @Bean
    public PagingQueryProvider CardFeeAdjustmentReaderQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);

        queryProvider.setSelectClause("""
            a.id AS adjustment_id,
            SUM(p.card_fee) AS category_fee_amount
        """);
        queryProvider.setFromClause("""
            FROM trade t
                JOIN payment p ON p.id = t.id
                JOIN store s ON s.id = t.store_id
                JOIN adjustment a ON a.store_id = s.id AND a.date_term = :dateTerm AND a.start_date = :startDate
        """);
        queryProvider.setWhereClause("""
            t.is_completed = 'Y' AND
            :startDate <= DATE(t.created_at) AND DATE(t.created_at) < :endDate
        """);
        queryProvider.setGroupClause("adjustment_id");

        Map<String, Order> sortKeys = new LinkedHashMap<>();
        sortKeys.put("adjustment_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<FeeAdjustmentDao> CardFeeAdjustmentWriter() {
        return new JdbcBatchItemWriterBuilder<FeeAdjustmentDao>()
            .dataSource(dataSource)
            .sql("INSERT INTO fee_adjustment (adjustment_id, fee_category, category_fee_amount)" +
                "VALUES (:adjustmentId, :feeCategory, :categoryFeeAmount)")
            .beanMapped()
            .build();
    }

    @Bean
    public Step createCardFeeAdjustmentItemStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) throws Exception {
        return new StepBuilder("createCardFeeAdjustmentItemStep", jobRepository)
            .<FeeAdjustmentItemDao, FeeAdjustmentItemDao>chunk(chunkSize, transactionManager)
            .reader(CardFeeAdjustmentItemReader(null, null, null))
            .writer(CardFeeAdjustmentItemWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<FeeAdjustmentItemDao> CardFeeAdjustmentItemReader(
        @Value("#{jobParameters[dateTerm]}") String dateTerm,
        @Value("#{jobParameters[startDate]}") LocalDate startDate,
        @Value("#{jobParameters[endDate]}") LocalDate endDate
    ) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("dateTerm", dateTerm);
        parameterValues.put("startDate", startDate);
        parameterValues.put("endDate", endDate);

        return new JdbcPagingItemReaderBuilder<FeeAdjustmentItemDao>()
            .pageSize(chunkSize)
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper((rs, rowNum) -> new FeeAdjustmentItemDao(
                rs.getLong("fee_adjustment_id"),
                rs.getString("service_provider_name"),
                rs.getInt("fee_amount")
            ))
            .queryProvider(CardFeeAdjustmentItemReaderQueryProvider())
            .parameterValues(parameterValues)
            .name("feeAdjustmentItemReader")
            .build();
    }

    @Bean
    public PagingQueryProvider CardFeeAdjustmentItemReaderQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);

        queryProvider.setSelectClause("""
            fa.id AS fee_adjustment_id,
            p.card_company AS service_provider_name,
            SUM(p.card_fee) AS fee_amount
        """);
        queryProvider.setFromClause("""
            FROM trade t
                JOIN payment p ON p.id = t.id
                JOIN store s ON s.id = t.store_id
                JOIN adjustment a ON a.store_id = s.id AND a.date_term = :dateTerm AND a.start_date = :startDate
                JOIN fee_adjustment fa ON fa.adjustment_id = a.id
        """);
        queryProvider.setWhereClause("""
            t.is_completed = 'Y' AND
            p.card_company IS NOT NULL AND
            :startDate <= DATE(t.created_at) AND DATE(t.created_at) < :endDate
        """);
        queryProvider.setGroupClause("fee_adjustment_id, service_provider_name");

        Map<String, Order> sortKeys = new LinkedHashMap<>();
        sortKeys.put("fee_adjustment_id", Order.ASCENDING);
        sortKeys.put("service_provider_name", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<FeeAdjustmentItemDao> CardFeeAdjustmentItemWriter() {
        return new JdbcBatchItemWriterBuilder<FeeAdjustmentItemDao>()
            .dataSource(dataSource)
            .sql("INSERT INTO fee_adjustment_item (fee_adjustment_id, service_provider_name, fee_amount)" +
                "VALUES (:feeAdjustmentId, :serviceProviderName, :feeAmount)")
            .beanMapped()
            .build();
    }

}
