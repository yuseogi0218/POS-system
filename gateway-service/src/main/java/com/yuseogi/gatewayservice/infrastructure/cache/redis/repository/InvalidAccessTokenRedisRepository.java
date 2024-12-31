package com.yuseogi.gatewayservice.infrastructure.cache.redis.repository;

import com.yuseogi.gatewayservice.infrastructure.cache.redis.dao.InvalidAccessToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InvalidAccessTokenRedisRepository extends CrudRepository<InvalidAccessToken, Long> {

    Optional<InvalidAccessToken> findByAccessToken(String accessToken);

}
