package com.yuseogi.batchserver.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    public static final String META_DATASOURCE = "metaDataSource";
    public static final String SERVICE_DATASOURCE = "serviceDataSource";

    @Bean(META_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.meta")
    public DataSource metaDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean(SERVICE_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.service")
    public DataSource serviceDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
    }

}
