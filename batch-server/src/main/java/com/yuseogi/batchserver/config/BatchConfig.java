package com.yuseogi.batchserver.config;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

    private final DataSource dataSource;
    private final PlatformTransactionManager platformTransactionManager;

    public BatchConfig(@Qualifier(DataSourceConfig.META_DATASOURCE) DataSource dataSource, @Qualifier(TransactionManagerConfig.META_TRANSACTION_MANAGER) PlatformTransactionManager platformTransactionManager) {
        this.dataSource = dataSource;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return platformTransactionManager;
    }
}
