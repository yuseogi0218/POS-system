package com.yuseogi.tradeservice.repository.implementation;

import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.repository.OrderDetailJdbcTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class OrderDetailJdbcTemplateRepositoryImpl implements OrderDetailJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<OrderDetailEntity> orderDetailList) {
        String sql = "INSERT INTO order_detail (order_id, product_id, count, total_amount, created_at) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
            sql,
            orderDetailList,
            orderDetailList.size(),
            (PreparedStatement ps, OrderDetailEntity orderDetail) -> {
               ps.setLong(1, orderDetail.getOrder().getId());
               ps.setLong(2, orderDetail.getProductId());
               ps.setInt(3, orderDetail.getCount());
               ps.setInt(4, orderDetail.getTotalAmount());
               ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            });
    }
}
