package soloproject.seomoim.KakaoApi.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class KakaoAddressSearchServiceTest {
    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService;

    @DisplayName("@Value 를 이용한 properties bind")
    @Test
    void valueBindTest() {
        assertThat(kakaoAddressSearchService.getKakaoRestApiKey()).isNotNull();
        log.info("apikey="+kakaoAddressSearchService.getKakaoRestApiKey());
    }
}

