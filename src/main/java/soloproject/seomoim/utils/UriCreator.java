package soloproject.seomoim.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UriCreator {

    public static URI createUri(String defaultUri,Long resourceId){
        return UriComponentsBuilder
                .newInstance()
                .path(defaultUri + "/{resource_id}")
                .buildAndExpand(resourceId)
                .toUri();
    }
}
