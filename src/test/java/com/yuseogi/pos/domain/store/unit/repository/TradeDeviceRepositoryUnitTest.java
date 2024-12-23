package com.yuseogi.pos.domain.store.unit.repository;

import com.yuseogi.pos.common.RepositoryUnitTest;
import com.yuseogi.pos.domain.store.repository.TradeDeviceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.stream.LongStream;

@EnableJpaRepositories(basePackageClasses = TradeDeviceRepository.class)
public class TradeDeviceRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private TradeDeviceRepository tradeDeviceRepository;

    @Test
    void findAllIdByStore() {
        // given
        Long storeId = 1L;
        List<Long> expectedTradeDeviceIdList = LongStream.range(1L, 11L).boxed().toList();

        // when
        List<Long> actualTradeDeviceIdList = tradeDeviceRepository.findAllIdByStoreId(storeId);

        // then
        Assertions.assertThat(actualTradeDeviceIdList).isEqualTo(expectedTradeDeviceIdList);
    }

}
