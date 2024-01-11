package soloproject.seomoim.moim.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import soloproject.seomoim.moim.dto.MoimDto;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LatestViewMoimService {

    private static final String LATEST_POST_KEY = "latest_view";

    private final RedisTemplate<String,Object> redisTemplateObject;

    private final ObjectMapper objectMapper;
    private ZSetOperations<String, Object> zSetOperations;

    @PostConstruct
    public void init() {
        this.zSetOperations = redisTemplateObject.opsForZSet();
    }

    public void addLatestViewMoim(Long memberId, MoimDto.Response moimResponse) throws JsonProcessingException {

        String key = LATEST_POST_KEY + ":" + memberId;
        zSetOperations.add(key,
                serializeMoimDto(moimResponse), System.currentTimeMillis());
    }

    public Set<MoimDto.Response> getLatestViewMoims(Long memberId, int count) throws JsonProcessingException {
        String key = LATEST_POST_KEY + ":" + memberId;

        Set<Object> jsonMoim = zSetOperations.reverseRange(key, 0, count - 1);

        Set<MoimDto.Response> latestViewMoims = new LinkedHashSet<>();

        if (jsonMoim == null) {
            return latestViewMoims;
        } else {
            for (Object moim : jsonMoim) {
                MoimDto.Response moimDto = deserializeMoimDto((String) moim);
                latestViewMoims.add(moimDto);
            }
            return latestViewMoims;
        }
    }

    public void deleteLatestPostsForMeember(Long memberId,int count) {
        String key = LATEST_POST_KEY + ":" + memberId;
        zSetOperations.reverseRange(key,0,count-1);
    }


    //객체 ->json 직렬화
    private String serializeMoimDto(MoimDto.Response moim) throws JsonProcessingException {
        return objectMapper.writeValueAsString(moim);
    }
    //json ->객체 역직렬화
    private MoimDto.Response deserializeMoimDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value,MoimDto.Response.class);
    }


}
