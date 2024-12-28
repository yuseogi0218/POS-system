package com.yuseogi.tradeservice.repository.implementation;

import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.repository.OrderDetailJdbcTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.util.List;

@RequiredArgsConstructor
public class OrderDetailJdbcTemplateRepositoryImpl implements OrderDetailJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<OrderDetailEntity> orderDetailList) {
        String sql = "INSERT INTO order_detail (order_id, product_name, product_category, product_price, count, total_amount) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
            sql,
            orderDetailList,
            orderDetailList.size(),
            (PreparedStatement ps, OrderDetailEntity orderDetail) -> {
               ps.setLong(1, orderDetail.getOrder().getId());
               ps.setString(2, orderDetail.getProductName());
               ps.setString(3, orderDetail.getProductCategory().name());
               ps.setInt(4, orderDetail.getProductPrice());
               ps.setInt(5, orderDetail.getCount());
               ps.setInt(6, orderDetail.getTotalAmount());
            });
    }
}
