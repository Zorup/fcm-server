package com.zorup.fcm.notification;

import com.zorup.fcm.FCMService;
import com.zorup.fcm.notification.Notification.UserInformation;
import com.zorup.fcm.util.MainModule;
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
    private final MainModule mainModule;
    private final FCMService fcmService;
    private final String CHAT_BASE_MESSAGE = "님으로부터 채팅이 도착했습니다.";
    private final String MENTION_BASE_MESSAGE = "님이 덧글을 남기셨습니다.";

    /**
     * 클라이언트쪽에서 요청시 데이터 포맷 다음의 형태로 감싸서 보내자(어쩌피 클라이언트쪽에서 미리 userList 받아서 가지고있음)
     * {
     *     sender : {userId : senderId,
     *               userName : senderName},
     *     receivers : [{userId : receiverId,
     *                   userName : receiverName},....
     *                 ],
     *     eventType : eventType
     * }
     *
     *  noti서버와 메인서버간 통신에서는, 토큰 정보만 가져와서 갱신
     * */
    public void saveNotification(Notification.NotificationRequest param){
        List<Notification> notifications = new ArrayList<>();
        UserInformation sender = param.getSender();
        List<UserInformation> receivers = param.getReceivers();
        Boolean eventType = param.getEventType();
        String content = getBaseMessage(eventType);

        makeNotificationEntity(notifications, sender, receivers, eventType, content);
        notificationRepository.saveAll(notifications);
    }



    private void makeNotificationEntity(List<Notification> notifications, UserInformation sender, List<UserInformation> receivers, Boolean eventType, String content) {
        for(UserInformation receiver : receivers){
            Notification notification = Notification.builder()
                    .senderId(sender.getUserId())
                    .eventType(eventType)
                    .receiverId(receiver.getUserId())
                    .readYn(false)
                    .content(sender.getUserName()+ content)
                    .build();
            notifications.add(notification);
        }
    }

    private String getBaseMessage(Boolean eventType) {
        if(Boolean.TRUE.equals(eventType)){
            return MENTION_BASE_MESSAGE;
        }else{
            return CHAT_BASE_MESSAGE;
        }
    }

}
