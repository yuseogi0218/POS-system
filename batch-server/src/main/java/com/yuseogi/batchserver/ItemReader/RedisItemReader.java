package com.yuseogi.batchserver.ItemReader;

import lombok.Builder;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisItemReader implements ItemReader<Map<String, Object>> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;
    private final String keyPattern;

    private int index = 0;
    private List<Map<String, Object>> data;

    @Builder(builderMethodName = "RedisItemReaderBuilder")
    public RedisItemReader(RedisTemplate<String, Object> redisTemplate, String keyPattern) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.keyPattern = keyPattern;
    }

    @Override
    public Map<String, Object> read() {
        if (data == null) {
            data = fetchFromRedis();
        }

        if (index < data.size()) {
            return data.get(index++);
        } else {
            return null; // 데이터가 끝났음을 알림
        }
    }

    private List<Map<String, Object>> fetchFromRedis() {
        List<Map<String, Object>> result = new ArrayList<>();

        // Redis에서 데이터를 읽어오는 로직
        Set<String> keys = redisTemplate.keys(keyPattern);
        for (String key : keys) {
            Map<String, Object> values = hashOperations.entries(key);
            if (!values.isEmpty()) {
                result.add(values);
            }
        }
        redisTemplate.delete(keys);

        return result;
    }
}
