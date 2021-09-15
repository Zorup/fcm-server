package com.zorup.fcm.notification;

import lombok.*;
import javax.persistence.*;
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
    private Long eventType;

    // 하이퍼링크?
    //일부 내용 첨가?
}
