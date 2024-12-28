package com.yuseogi.userservice.infrastructure.cache.redis.repository;

import com.yuseogi.userservice.infrastructure.cache.redis.dao.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
