package com.zorup.fcm.notification;

import lombok.*;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "noti_tb")
public class Notification implements Serializable {
    private static final long serialVersionUID = -1898223676304540998L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column
    private Long senderId;

    @Column
    private Long receiverId;

    @Column
    private Boolean eventType;

    @Column
    private String content;

    @Column
    private Boolean readYn;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Transactional
    public static class NotificationRequest{
        private Long senderId;
        private Long[] receiverIds;
        private Boolean eventType;
    }

}
