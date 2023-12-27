package soloproject.seomoim.moim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import soloproject.seomoim.moim.dto.MoimDto;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LatestViewService {

    private static final String LATEST_POST_KEY = "latest_view";

    private final RedisTemplate redisTemplateObject;

    public void addLatestPostForMember(Long memberId, MoimDto.Response moimResponse) {

            String memberKey = LATEST_POST_KEY + ":" + memberId;
            ZSetOperations<String, Object> zSetOps = redisTemplateObject.opsForZSet();
            zSetOps.add(memberKey, moimResponse, System.currentTimeMillis());
    }

    public Set<Object> getLatestPostsForMember(Long memberId, int count) {
        String memberKey = LATEST_POST_KEY + ":" + memberId;

        ZSetOperations<String, Object> zSetOps = redisTemplateObject.opsForZSet();

        return zSetOps.reverseRange(memberKey, 0, count - 1);
    }
}
