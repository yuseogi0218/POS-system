package com.yuseogi.tradeservice.unit.repository;

import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.repository.implementation.OrderDetailJdbcTemplateRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

public class OrderDetailJdbcTemplateRepositoryUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private OrderDetailJdbcTemplateRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveAll() {
        // given
        OrderDetailEntity orderDetail1 = mock(OrderDetailEntity.class);
        OrderDetailEntity orderDetail2 = mock(OrderDetailEntity.class);
        List<OrderDetailEntity> orderDetailList = List.of(orderDetail1, orderDetail2);

        // when
        repository.saveAll(orderDetailList);

        // then
        verify(jdbcTemplate, times(1)).batchUpdate(
            eq("INSERT INTO order_detail (order_id, product_id, count, total_amount, created_at) VALUES (?, ?, ?, ?, ?, ?)"),
            eq(orderDetailList),
            eq(orderDetailList.size()),
            any()
        );
    }
}
