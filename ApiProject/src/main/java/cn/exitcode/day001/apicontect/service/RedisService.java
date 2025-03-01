package cn.exitcode.day001.apicontect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setCode(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String getCode(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void deleteCode(String key) {
        stringRedisTemplate.delete(key);
    }
}
