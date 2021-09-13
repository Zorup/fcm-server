package com.zorup.fcm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/v1")
public class FCMController {
    private final FCMService fcmService;

    @PostMapping("/fcm-msg")
    public boolean sendFcmMessage(String pushToken){
        Map<String, String> eventMap = new HashMap<>();
        fcmService.sendNotification(pushToken, "mention", "사용자 멘션", eventMap);
        return true;
    }
}
