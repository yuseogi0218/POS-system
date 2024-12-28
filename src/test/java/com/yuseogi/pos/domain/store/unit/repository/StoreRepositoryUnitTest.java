package com.yuseogi.pos.domain.store.unit.repository;

import com.yuseogi.pos.gateway.RepositoryUnitTest;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.StoreEntityBuilder;
import com.yuseogi.pos.domain.store.repository.StoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = StoreRepository.class)
public class StoreRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void findFirstByOwnerUserEmail_존재_O() {
        // given
        StoreEntity expectedStore = StoreEntityBuilder.build();
        String ownerUserEmail = expectedStore.getOwnerUser().getEmail();

        // when
        Optional<StoreEntity> optionalStore = storeRepository.findFirstByOwnerUserEmail(ownerUserEmail);

        // then
        Assertions.assertThat(optionalStore.isPresent()).isTrue();
        optionalStore.ifPresent(
            actualStore -> StoreEntityBuilder.assertStore(actualStore, expectedStore)
        );
    }

    @Test
    void findFirstByOwnerUserEmail_존재_X() {
        // given
        String unknownUserEmail = "unknown-user@domain.com";

        // when
        Optional<StoreEntity> optionalStore = storeRepository.findFirstByOwnerUserEmail(unknownUserEmail);

        // then
        Assertions.assertThat(optionalStore.isEmpty()).isTrue();
    }

    @Test
    void findFirstByTradeDeviceId_존재_O() {
        // given
        Long tradeDeviceId = 1L;
        StoreEntity expectedStore = StoreEntityBuilder.build();

        // when
        Optional<StoreEntity> optionalStore = storeRepository.findFirstByTradeDeviceId(tradeDeviceId);

        // then
        Assertions.assertThat(optionalStore.isPresent()).isTrue();
        optionalStore.ifPresent(
            actualStore -> StoreEntityBuilder.assertStore(actualStore, expectedStore)
        );
    }

    @Test
    void findFirstByTradeDeviceId_존재_X() {
        // given
        Long unknownTradeDeviceId = 0L;

        // when
        Optional<StoreEntity> optionalStore = storeRepository.findFirstByTradeDeviceId(unknownTradeDeviceId);

        // then
        Assertions.assertThat(optionalStore.isEmpty()).isTrue();
    }
}
