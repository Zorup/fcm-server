package com.zorup.fcm.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zorup.fcm.FCMService;
import com.zorup.fcm.notification.Notification.NotificationRequest;
import com.zorup.fcm.notification.Notification.UserInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;
    private final String CHAT_BASE_MESSAGE = "님으로부터 채팅이 도착했습니다.";
    private final String MENTION_BASE_MESSAGE = "님이 덧글을 남기셨습니다.";


    public void saveNotification(NotificationRequest param) throws JsonProcessingException {
        List<Notification> notifications = new ArrayList<>();
        UserInformation sender = param.getSender();
        List<UserInformation> receivers = param.getReceivers();
        Boolean eventType = param.getEventType();
        String content = getBaseMessage(eventType);
        Long[] receiverIds = makeNotificationEntity(notifications, sender, receivers, eventType, content);
        notificationRepository.saveAll(notifications);
        log.info("SUCCESS :: save notification data");

        log.info("START :: send Web Push Message");
        fcmService.sendNotifications(sender.getUserId(), receiverIds, "Mention", "웹 푸쉬");
        log.info("SUCCESS :: push Web Message");
    }

    private Long[] makeNotificationEntity(List<Notification> notifications, UserInformation sender, List<UserInformation> receivers, Boolean eventType, String content) {
        List<Long> receiverIds = new ArrayList<>();
        for(UserInformation receiver : receivers){
            Notification notification = Notification.builder()
                    .senderId(sender.getUserId())
                    .eventType(eventType)
                    .receiverId(receiver.getUserId())
                    .readYn(false)
                    .content(sender.getUserName()+ content)
                    .build();
            notifications.add(notification);
            receiverIds.add(receiver.getUserId());
        }
        return receiverIds.toArray(new Long[receiverIds.size()]);
    }

    private String getBaseMessage(Boolean eventType) {
        if(Boolean.TRUE.equals(eventType)){
            return MENTION_BASE_MESSAGE;
        }else{
            return CHAT_BASE_MESSAGE;
        }
    }
}
