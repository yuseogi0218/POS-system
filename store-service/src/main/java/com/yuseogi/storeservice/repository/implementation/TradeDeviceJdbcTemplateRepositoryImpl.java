package com.yuseogi.storeservice.repository.implementation;

import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.repository.TradeDeviceJdbcTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.util.List;

@RequiredArgsConstructor
public class TradeDeviceJdbcTemplateRepositoryImpl implements TradeDeviceJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<TradeDeviceEntity> tradeDeviceList) {
        String sql = "INSERT INTO trade_device (store_id) VALUES (?)";

        jdbcTemplate.batchUpdate(
            sql,
            tradeDeviceList,
            tradeDeviceList.size(),
            (PreparedStatement ps, TradeDeviceEntity tradeDevice) -> {
                ps.setLong(1, tradeDevice.getStore().getId());
            });
    }
}
