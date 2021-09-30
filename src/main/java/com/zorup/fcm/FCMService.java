package com.zorup.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.zorup.fcm.util.MainModule;
import com.zorup.fcm.util.TokenQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zorup.fcm.util.TokenQueryResponse.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    @Autowired
    Environment env;
    @Autowired
    ObjectMapper objectMapper;
    FirebaseMessaging instance;
    private final MainModule mainModule;

    @PostConstruct
    private void init() {
        try {
            FileInputStream refreshToken = new FileInputStream(env.getProperty("fcm.keypath"));
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .build();
            this.instance = FirebaseMessaging.getInstance(FirebaseApp.initializeApp(options));
            log.info("FCM BEAN Set Connect..");
        }catch (IOException e){
            log.info("Error : FCM 설정 오류");
            e.printStackTrace();
        }
    }

    public void sendNotifications(Long senderId, Long[] receiverIds,
                                  String event, String content,
                                  LocalDateTime createTime,
                                  Map<Long, Long> userNotificationInfo) throws JsonProcessingException {

        List<UserTokenInfo> userTokenInfos = mainModule.getUserPushTokenByUserIds(receiverIds);
        List<Message> msgs = new ArrayList<>();

        Map<String, String> data = new HashMap<>();
        data.put("content", content);
        data.put("readYn", "false");
        //프론트단에서 역직렬화해서 풀어서써야됨
        data.put("createDate", objectMapper.writeValueAsString(createTime));

        for(UserTokenInfo userTokenInfo : userTokenInfos) {
            if(userTokenInfo.getPushToken() == null) continue;
            data.remove("notificationId");
            data.put("notificationId", userNotificationInfo.get(userTokenInfo.getUserId()).toString());

            Message msg = Message.builder()
                    .setNotification(
                            Notification.builder()
                                    .setTitle(event)
                                    .setBody(content)
                                    .build()
                    )
                    .setToken(userTokenInfo.getPushToken())
                    .putAllData(data)
                    .build();
            msgs.add(msg);
        }
        try {
            instance.sendAll(msgs);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage());
            }
        }
    }
}
