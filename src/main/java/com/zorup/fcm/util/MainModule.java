package com.zorup.fcm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorup.fcm.util.TokenQueryResponse.UserTokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainModule {
    @Autowired
    Environment env;
    private final ObjectMapper objectMapper;

    public List<UserTokenInfo> getUserPushTokenByUserIds(Long[] userIds) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(env.getProperty("main-api-url"));
        for(Long userId : userIds){
            uri.queryParam("userId", userId);
        }
        ResponseEntity<String> restResponse;
        restResponse = restTemplate.getForEntity(uri.toUriString(), String.class);
        TokenQueryResponse tokenQueryResponse = objectMapper.readValue(restResponse.getBody(), TokenQueryResponse.class);
        return tokenQueryResponse.getList();
    }
}
