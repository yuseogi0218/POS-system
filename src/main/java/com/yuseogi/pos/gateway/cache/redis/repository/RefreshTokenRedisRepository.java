package com.yuseogi.pos.gateway.cache.redis.repository;

import com.yuseogi.pos.gateway.cache.redis.dao.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
