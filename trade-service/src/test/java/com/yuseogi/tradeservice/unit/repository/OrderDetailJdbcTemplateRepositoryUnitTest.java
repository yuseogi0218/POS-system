package com.yuseogi.tradeservice.unit.repository;

import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderEntity;
import com.yuseogi.tradeservice.repository.implementation.OrderDetailJdbcTemplateRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

public class OrderDetailJdbcTemplateRepositoryUnitTest extends JdbcTemplateRepositoryUnitTest {

    private OrderDetailJdbcTemplateRepositoryImpl repository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.repository = new OrderDetailJdbcTemplateRepositoryImpl(jdbcTemplate);
    }

    @Test
    void saveAll() {
        // given
        Long orderId = 1L;
        OrderEntity order = mock(OrderEntity.class);
        when(order.getId()).thenReturn(orderId);

        Long productId1 = 1L;
        Integer count1 = 10;
        Integer totalAmount1 = 10000;
        OrderDetailEntity orderDetail1 = mock(OrderDetailEntity.class);

        Long productId2 = 2L;
        Integer count2 = 20;
        Integer totalAmount2 = 20000;
        OrderDetailEntity orderDetail2 = mock(OrderDetailEntity.class);
        List<OrderDetailEntity> orderDetailList = List.of(orderDetail1, orderDetail2);

        // stub
        when(orderDetail1.getOrder()).thenReturn(order);
        when(orderDetail1.getProductId()).thenReturn(productId1);
        when(orderDetail1.getCount()).thenReturn(count1);
        when(orderDetail1.getTotalAmount()).thenReturn(totalAmount1);

        when(orderDetail2.getOrder()).thenReturn(order);
        when(orderDetail2.getProductId()).thenReturn(productId2);
        when(orderDetail2.getCount()).thenReturn(count2);
        when(orderDetail2.getTotalAmount()).thenReturn(totalAmount2);

        // when
        repository.saveAll(orderDetailList);

        // then
    }
}
