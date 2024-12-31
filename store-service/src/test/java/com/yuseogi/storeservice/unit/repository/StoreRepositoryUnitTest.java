package com.yuseogi.storeservice.unit.repository;

import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.StoreEntityBuilder;
import com.yuseogi.storeservice.repository.StoreRepository;
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
    void findFirstById_존재_O() {
        // given
        Long storeId = 1L;
        StoreEntity expectedStore = StoreEntityBuilder.build();

        // when
        Optional<StoreEntity> optionalStore = storeRepository.findFirstById(storeId);

        // then
        Assertions.assertThat(optionalStore.isPresent()).isTrue();
        optionalStore.ifPresent(
            actualStore -> {
                Assertions.assertThat(actualStore.getId()).isEqualTo(storeId);
                StoreEntityBuilder.assertStore(actualStore, expectedStore);
            }
        );
    }

    @Test
    void findFirstById_존재_X() {
        // given
        Long unknownStoreId = 0L;

        // when
        Optional<StoreEntity> optionalStore = storeRepository.findFirstById(unknownStoreId);

        // then
        Assertions.assertThat(optionalStore.isEmpty()).isTrue();
    }

    @Test
    void findFirstByOwnerUser_존재_O() {
        // given
        Long ownerUserId = 1L;
        StoreEntity expectedStore = StoreEntityBuilder.build();

        // when
        Optional<StoreEntity> optionalStore = storeRepository.findFirstByOwnerUser(ownerUserId);

        // then
        Assertions.assertThat(optionalStore.isPresent()).isTrue();
        optionalStore.ifPresent(
            actualStore -> {
                Assertions.assertThat(actualStore.getOwnerUserId()).isEqualTo(ownerUserId);
                StoreEntityBuilder.assertStore(actualStore, expectedStore);
            }
        );
    }

    @Test
    void findFirstByOwnerUser_존재_X() {
        // given
        Long unknownUserId = 0L;

        // when
        Optional<StoreEntity> optionalStore = storeRepository.findFirstByOwnerUser(unknownUserId);

        // then
        Assertions.assertThat(optionalStore.isEmpty()).isTrue();
    }

}
