package com.yuseogi.storeservice.unit.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class JdbcTemplateRepositoryUnitTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

}
