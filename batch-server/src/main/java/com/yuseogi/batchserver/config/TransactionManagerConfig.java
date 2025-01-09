package com.yuseogi.batchserver.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static com.yuseogi.batchserver.config.DataSourceConfig.META_DATASOURCE;

@Configuration
public class TransactionManagerConfig {

    public static final String META_TRANSACTION_MANAGER = "metaTransactionManager";

    @Bean(name = META_TRANSACTION_MANAGER)
    public PlatformTransactionManager metaTransactionManager(@Qualifier(META_DATASOURCE) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
