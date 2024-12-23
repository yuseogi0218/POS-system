package com.yuseogi.pos.domain.store.unit.entity;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.type.PosGrade;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class StoreEntityUnitTest {

    @Test
    void constructor() {
        // given
        UserEntity expectedUser = mock(UserEntity.class);
        String expectedName = "상점 이름";
        PosGrade expectedPosGrade = PosGrade.BRONZE;

        // when
        StoreEntity actualStore = new StoreEntity(expectedUser, expectedName, expectedPosGrade);

        // then
        Assertions.assertThat(actualStore.getOwnerUser()).isEqualTo(expectedUser);
        Assertions.assertThat(actualStore.getName()).isEqualTo(expectedName);
        Assertions.assertThat(actualStore.getPosGrade()).isEqualTo(expectedPosGrade);
    }
}
