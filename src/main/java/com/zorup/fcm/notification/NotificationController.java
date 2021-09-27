package com.zorup.fcm.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zorup.fcm.notification.Notification.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/v1")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/fcm-msg")
    public boolean sendFcmMessage(@RequestBody NotificationRequest param) throws JsonProcessingException {
        notificationService.saveNotification(param);
        return true;
    }
}