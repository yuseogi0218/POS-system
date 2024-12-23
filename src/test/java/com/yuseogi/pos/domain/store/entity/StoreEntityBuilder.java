package com.yuseogi.pos.domain.store.entity;

import com.yuseogi.pos.domain.store.entity.type.PosGrade;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import com.yuseogi.pos.domain.user.entity.UserEntityBuilder;
import org.assertj.core.api.Assertions;

public class StoreEntityBuilder {
    public static StoreEntity build() {
        UserEntity ownerUser = UserEntityBuilder.build();

        return StoreEntity.builder()
            .ownerUser(ownerUser)
            .name("상점 이름")
            .posGrade(PosGrade.BRONZE)
            .build();
    }

    public static void assertStore(StoreEntity actualStore, StoreEntity expectedStore) {
        UserEntityBuilder.assertUser(actualStore.getOwnerUser(), expectedStore.getOwnerUser());
        Assertions.assertThat(actualStore.getName()).isEqualTo(expectedStore.getName());
        Assertions.assertThat(actualStore.getPosGrade()).isEqualTo(expectedStore.getPosGrade());
    }
}
