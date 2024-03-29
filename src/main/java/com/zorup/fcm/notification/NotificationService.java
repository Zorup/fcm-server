package com.zorup.fcm.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zorup.fcm.FCMService;
import com.zorup.fcm.notification.Notification.NotificationRequest;
import com.zorup.fcm.notification.Notification.UserInformation;
import com.zorup.fcm.util.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zorup.fcm.util.TokenQueryResponse.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final RedisRepository redisRepository;
    private final FCMService fcmService;
    private final String CHAT_BASE_MESSAGE = "님으로부터 채팅이 도착했습니다.";
    private final String MENTION_BASE_MESSAGE = "님이 덧글을 남기셨습니다.";


    public void setUserPushToken(Long userId, String token){
        UserTokenInfo userTokenInfo = new UserTokenInfo();
        userTokenInfo.setUserId(userId);
        userTokenInfo.setPushToken(token);
        redisRepository.insertPushToken(userTokenInfo);
    }

    public void deleteUserPushToken(Long userId){
        redisRepository.deletePushToken(userId);
    }

    public void saveNotification(NotificationRequest param) throws JsonProcessingException {
        List<Notification> notifications = new ArrayList<>();
        Map<Long, Long> userNotificationInfo = new HashMap<>();
        UserInformation sender = param.getSender();
        List<UserInformation> receivers = param.getReceivers();
        Boolean eventType = param.getEventType();
        Long postId = param.getPostId();

        String content = getBaseMessage(eventType);
        content = sender.getUserName()+ content;

        LocalDateTime createTime = LocalDateTime.now();
        List<Long> receiverIds = makeNotificationEntity(notifications, sender, receivers, eventType, content, createTime, postId);
        notificationRepository.saveAll(notifications);

        log.info("SUCCESS :: save notification data");

        for(Notification notification : notifications){
            userNotificationInfo.put(notification.getReceiverId(), notification.getNotificationId());
        }

        log.info("START :: send Web Push Message");
        fcmService.sendNotifications(sender.getUserId(), receiverIds, "Mention", content, createTime, userNotificationInfo, postId);
        log.info("SUCCESS :: push Web Message");
    }

    public List<NotificationProjection> getUserNotificationList(Long receiverId){
        return notificationRepository.findByReceiverIdAndEventTypeIsTrue(receiverId);
    }

    public boolean patchNotificationReadYn(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(IllegalAccessError::new);
        notification.setReadYn(true);
        notificationRepository.save(notification);
        return true;
    }

    private List<Long> makeNotificationEntity(List<Notification> notifications, UserInformation sender,
                                              List<UserInformation> receivers, Boolean eventType,
                                              String content, LocalDateTime createTime, Long postId) {
        List<Long> receiverIds = new ArrayList<>();
        for(UserInformation receiver : receivers){
            Notification notification = Notification.builder()
                    .senderId(sender.getUserId())
                    .eventType(eventType)
                    .receiverId(receiver.getUserId())
                    .readYn(false)
                    .content(content)
                    .createDate(createTime)
                    .postId(postId)
                    .build();
            notifications.add(notification);
            receiverIds.add(receiver.getUserId());
        }
        return receiverIds;
    }

    private String getBaseMessage(Boolean eventType) {
        if(Boolean.TRUE.equals(eventType)){
            return MENTION_BASE_MESSAGE;
        }else{
            return CHAT_BASE_MESSAGE;
        }
    }
}
