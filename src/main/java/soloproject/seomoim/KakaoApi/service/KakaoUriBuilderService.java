package soloproject.seomoim.KakaoApi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class KakaoUriBuilderService {

    private static final String KAKAO_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    public URI buildUriForAddressSearch(String address) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_SEARCH_ADDRESS_URL);

        return uriBuilder.queryParam("query", address).build().encode().toUri();
    }
}
