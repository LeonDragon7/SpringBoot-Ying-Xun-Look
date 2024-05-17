package com.dragon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisApi {

    @Autowired
    private RedisTemplate redisTemplate;
    public String getString(String key) {
        try {
            return (String) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.warn("redis getString error key={}", key, e);
            return null;
        }
    }

    public void setValue(String key, String value, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, time, unit);
        } catch (Exception e) {
            log.warn("redis setValue error key={}, value={}", key, value, e);
        }
    }
}
