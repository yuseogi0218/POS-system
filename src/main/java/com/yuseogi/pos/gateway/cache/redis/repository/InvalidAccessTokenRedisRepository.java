package com.yuseogi.pos.gateway.cache.redis.repository;

import com.yuseogi.pos.gateway.cache.redis.dao.InvalidAccessToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InvalidAccessTokenRedisRepository extends CrudRepository<InvalidAccessToken, String> {

    Optional<InvalidAccessToken> findByAccessToken(String accessToken);

}
