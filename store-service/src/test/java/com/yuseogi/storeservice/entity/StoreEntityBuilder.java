package com.yuseogi.storeservice.entity;

import com.yuseogi.storeservice.entity.type.PosGrade;
import org.assertj.core.api.Assertions;

public class StoreEntityBuilder {
    public static StoreEntity build() {
        Long expectedOwnerUserId = 1L;

        return StoreEntity.builder()
            .ownerUserId(expectedOwnerUserId)
            .name("상점 이름")
            .posGrade(PosGrade.BRONZE)
            .settlementDate(1)
            .build();
    }

    public static void assertStore(StoreEntity actualStore, StoreEntity expectedStore) {
        Assertions.assertThat(actualStore.getOwnerUserId()).isEqualTo(expectedStore.getOwnerUserId());
        Assertions.assertThat(actualStore.getName()).isEqualTo(expectedStore.getName());
        Assertions.assertThat(actualStore.getPosGrade()).isEqualTo(expectedStore.getPosGrade());
        Assertions.assertThat(actualStore.getSettlementDate()).isEqualTo(expectedStore.getSettlementDate());
    }
}
