package soloproject.seomoim.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate redisTemplate;

    public void set(String email, String authNumber, int minutes) {
        redisTemplate.opsForValue().set(email, authNumber, minutes, TimeUnit.MINUTES);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key){
        return redisTemplate.delete(key);
    }

    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }


}