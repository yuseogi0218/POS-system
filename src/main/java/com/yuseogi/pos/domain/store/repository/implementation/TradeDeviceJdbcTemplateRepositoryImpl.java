package com.yuseogi.pos.domain.store.repository.implementation;

import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.repository.TradeDeviceJdbcTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@RequiredArgsConstructor
public class TradeDeviceJdbcTemplateRepositoryImpl implements TradeDeviceJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<TradeDeviceEntity> tradeDeviceList) {
        String sql = "INSERT INTO TradeDevice (storeId) VALUES (?)";

        jdbcTemplate.batchUpdate(
            sql,
            tradeDeviceList,
            tradeDeviceList.size(),
            (PreparedStatement ps, TradeDeviceEntity tradeDevice) -> {
                ps.setLong(1, tradeDevice.getStore().getId());
            });
    }
}
