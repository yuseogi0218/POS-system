package com.yuseogi.batchserver.component.implementation;

import com.yuseogi.batchserver.component.SchedulerJdbcTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SchedulerJdbcTemplateRepositoryImpl implements SchedulerJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> findAllStoreId() {
        return jdbcTemplate.query("SELECT id FROM store", (rs, rowNum) -> rs.getLong("id"));
    }
}
