package com.zorup.fcm.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class MainModule {
    @Autowired
    Environment env;

    /**
     * accessToken : 로그인한 사용자의 JWT 토큰
     * token : Firebase app init 시 개별 사용자에게 발급된 토큰
     * */

    public Long getUserIdByFcmToken(String accessToken, String token){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity restRequest = setHeader(accessToken);
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(env.getProperty("main-api-url"))
                .queryParam("push-token", token);
        ResponseEntity<String> restReponse;
        restReponse = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, restRequest, String.class);

        return 1L;
    }

    public HttpEntity setHeader(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", accessToken);
        return new HttpEntity (headers);
    }
}
