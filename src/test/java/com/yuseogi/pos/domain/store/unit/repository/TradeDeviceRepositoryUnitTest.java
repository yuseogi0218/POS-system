package com.yuseogi.pos.domain.store.unit.repository;

import com.yuseogi.pos.gateway.RepositoryUnitTest;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntityBuilder;
import com.yuseogi.pos.domain.store.repository.TradeDeviceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

@EnableJpaRepositories(basePackageClasses = TradeDeviceRepository.class)
public class TradeDeviceRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private TradeDeviceRepository tradeDeviceRepository;

    @Test
    void findFirstById_존재_O() {
        // given
        Long tradeDeviceId = 1L;
        TradeDeviceEntity expectedTradeDevice = TradeDeviceEntityBuilder.build();

        // when
        Optional<TradeDeviceEntity> optionalTradeDevice = tradeDeviceRepository.findFirstById(tradeDeviceId);

        // then
        Assertions.assertThat(optionalTradeDevice.isPresent()).isTrue();
        optionalTradeDevice.ifPresent(
            actualTradeDevice -> {
                Assertions.assertThat(actualTradeDevice.getId()).isEqualTo(tradeDeviceId);
                TradeDeviceEntityBuilder.assertTradeDevice(actualTradeDevice, expectedTradeDevice);
            }
        );
    }

    @Test
    void findFirstById_존재_X() {
        // given
        Long unknownTradeDeviceId = 0L;

        // when
        Optional<TradeDeviceEntity> optionalTradeDevice = tradeDeviceRepository.findFirstById(unknownTradeDeviceId);

        // then
        Assertions.assertThat(optionalTradeDevice.isEmpty()).isTrue();
    }

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
