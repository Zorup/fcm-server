package com.zorup.fcm.notification;

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
        Long senderId = param.getSenderId();
        Boolean eventType = param.getEventType();
        Long[] receiverIds = param.getReceiverIds();

        for(Long receiverId : receiverIds){
            Notification notification = Notification.builder()
                    .senderId(senderId)
                    .eventType(eventType)
                    .receiverId(receiverId)
                    .readYn(false)
                    .content(null)
                    .build();
        }
    }

}
