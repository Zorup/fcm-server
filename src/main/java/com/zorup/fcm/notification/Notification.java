package com.zorup.fcm.notification;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column
    private LocalDateTime createDate;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationRequest{
        private UserInformation sender;
        private List<UserInformation> receivers;
        private Boolean eventType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInformation{
        private Long userId;
        private String userName;
    }
}