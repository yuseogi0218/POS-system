package com.yuseogi.pos.domain.user.unit.entity;

import com.yuseogi.pos.domain.user.entity.UserEntity;
import com.yuseogi.pos.domain.user.entity.UserEntityBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserEntityUnitTest {

    @Test
    public void extendsUserDetails() {
        // given
        UserEntity user = UserEntityBuilder.build();

        // when & then
        Assertions.assertThat(user.getAuthorities().size()).isEqualTo(1);
        Assertions.assertThat(user.getAuthorities().toArray()).contains(new SimpleGrantedAuthority("ROLE_STORE_OWNER"));

        Assertions.assertThat(user.getUsername()).isEqualTo(user.getEmail());
        Assertions.assertThat(user.getPassword()).isEqualTo("");
        Assertions.assertThat(user.isAccountNonExpired()).isTrue();
        Assertions.assertThat(user.isAccountNonLocked()).isTrue();
        Assertions.assertThat(user.isCredentialsNonExpired()).isTrue();
        Assertions.assertThat(user.isEnabled()).isTrue();
    }
}
