package qiwen.com.library.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import qiwen.com.library.service.impl.LibBookServiceImpl;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Value(value = "${spring.application.name}")
    private String REDIS_PREFIX;
    @Autowired
    RedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LibBookServiceImpl.class);

    public Object get(String key){
        key = wrapRedisKey(key);
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public boolean set(String key,Object value){
        try {
            key = wrapRedisKey(key);
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            logger.error("redis set value exception:{}",e);
            return false;
        }
    }

    public boolean setex(String key,Object value,long expire){
        try {
            key = wrapRedisKey(key);
            redisTemplate.opsForValue().set(key,value,expire, TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            logger.error("redis set value and expire exception:{}",e);
            return false;
        }
    }

    public boolean expire(String key,long expire){
        try {
            key = wrapRedisKey(key);
            redisTemplate.expire(key,expire, TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            logger.error("redis set key expire exception:{}",e);
            return false;
        }
    }

    public boolean del(String key){
        try {
            key = wrapRedisKey(key);
            redisTemplate.delete(key);
            return true;
        }catch (Exception e){
            logger.error("redis delete key exception:{}",e);
            return false;
        }
    }

    private String wrapRedisKey(String key) {
        return REDIS_PREFIX + ":" + key;
    }

}
