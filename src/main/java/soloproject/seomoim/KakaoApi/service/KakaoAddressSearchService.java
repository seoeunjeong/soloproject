package soloproject.seomoim.KakaoApi.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import soloproject.seomoim.KakaoApi.dto.KakaoApiResponseDto;

import java.net.URI;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class KakaoAddressSearchService {

    private final RestTemplate restTemplate;

    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("${kakao.api.key}")
    private String kakaoRestApiKey;

    @Retryable(
            include = {RuntimeException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 2000)
    )
    public KakaoApiResponseDto requestAddressSearch(String address) {

        if (ObjectUtils.isEmpty(address)) return null;

        URI uri = kakaoUriBuilderService.buildUriForAddressSearch(address);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();

    }

    /*return 타입을 맞춰주지않을 경우 에러 발생 */
    @Recover
    public KakaoApiResponseDto recover(RuntimeException e,String address){
        log.error("All the retries failed. address:{},error={}",address,e.getMessage());
        return null;
    }
}
