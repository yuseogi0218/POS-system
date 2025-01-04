package com.yuseogi.storeservice.unit.entity;

import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.type.PosGrade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class StoreEntityUnitTest {

    @Test
    void constructor() {
        // given
        Long expectedOwnerUserId = 1L;
        String expectedName = "상점 이름";
        PosGrade expectedPosGrade = PosGrade.BRONZE;
        Integer expectedSettlementDate = 1;

        // when
        StoreEntity actualStore = new StoreEntity(expectedOwnerUserId, expectedName, expectedPosGrade, expectedSettlementDate);

        // then
        Assertions.assertThat(actualStore.getOwnerUserId()).isEqualTo(expectedOwnerUserId);
        Assertions.assertThat(actualStore.getName()).isEqualTo(expectedName);
        Assertions.assertThat(actualStore.getPosGrade()).isEqualTo(expectedPosGrade);
        Assertions.assertThat(actualStore.getSettlementDate()).isEqualTo(expectedSettlementDate);
    }
}
