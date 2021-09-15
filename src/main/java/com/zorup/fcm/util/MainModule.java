package com.zorup.fcm.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MainModule {
    @Autowired
    Environment env;

    /**
     * accessToken : 로그인한 사용자의 JWT 토큰
     * token : Firebase app init 시 개별 사용자에게 발급된 토큰
     * */

    public Long getUserIdByFcmToken(String token){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(env.getProperty("main-api-url"))
                .queryParam("push-token", token);
        ResponseEntity<String> restResponse;
        restResponse = restTemplate.getForEntity(uri.toUriString(), String.class);
        Map<String, String> responseBody = jsonStringToHashMap(restResponse.getBody());
        return Long.parseLong(responseBody.get("data"));
    }

    private HashMap<String, String> jsonStringToHashMap(String jsonString) {
        HashMap<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(jsonString, new TypeReference<HashMap<String, String>>() {
            });
        } catch (Exception e) {
            log.info("Exception converting {} to map", jsonString, e);
        }
        return map;
    }

}
