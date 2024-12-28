package com.yuseogi.userservice.repository;

import com.yuseogi.userservice.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findFirstByEmail(String email);
}
