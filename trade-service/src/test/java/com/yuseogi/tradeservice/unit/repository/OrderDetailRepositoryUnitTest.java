package com.yuseogi.tradeservice.unit.repository;

import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderDetailEntityBuilder;
import com.yuseogi.tradeservice.repository.OrderDetailRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories(basePackageClasses = OrderDetailRepository.class)
public class OrderDetailRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    void findAllByOrderId() {
        // given
        Long orderId = 1L;
        List<OrderDetailEntity> expectedOrderDetailList = OrderDetailEntityBuilder.buildList();
        Integer expectedListSize = expectedOrderDetailList.size();

        // when
        List<OrderDetailEntity> actualOrderDetailList = orderDetailRepository.findAllByOrderId(orderId);

        // then
        Assertions.assertThat(actualOrderDetailList.size()).isEqualTo(expectedListSize);
        for (int i = 0; i < expectedListSize; i++) {
            OrderDetailEntityBuilder.assertOrderDetail(actualOrderDetailList.get(i), expectedOrderDetailList.get(i));
        }
    }
}
