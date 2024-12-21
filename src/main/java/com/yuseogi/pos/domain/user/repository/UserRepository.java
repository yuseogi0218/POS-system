package com.yuseogi.pos.domain.user.repository;

import com.yuseogi.pos.domain.user.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findFirstByEmail(String email);
}
