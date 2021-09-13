package com.zorup.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class FCMService {
    @Autowired
    Environment env;
    FirebaseMessaging instance;

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


    //실질적으로 원하는 서비스는 메시지니까 데이터 말고 메시지 세팅해야됨
    //data는 해당 알람과 관련된 url등을 보내는 식으로 활용 가능하다하네.
    public void sendNotification(String userPushToken,String title, String content, Map<String, String> eventMap){
        Message msg = Message.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(content)
                                .build()
                )
                .setToken(userPushToken) //어떤 사용자에게 보낼 것인가?, 로그인시 생성된 jwt토큰 재활용
                .putAllData(eventMap) //실제 해당 사용자에게 전달될 데이터
                .build();
        try {
            instance.send(msg);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage());
            }
        }
    }
}
