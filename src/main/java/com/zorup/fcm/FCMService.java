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

    /**
     * TODO
     *   public BatchResponse sendAll(
     *       @NonNull List<Message> messages, boolean dryRun) throws FirebaseMessagingException {
     *     return sendAllOp(messages, dryRun).call();
     *   }
     * 관련해서 Documentation 보고 참고해서 여러 메시지 한번에 처리 가능하도록 바꾸고 테스트 필요
     * */
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
