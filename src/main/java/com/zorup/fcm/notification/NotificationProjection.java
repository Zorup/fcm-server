package com.zorup.fcm.notification;

import java.time.LocalDateTime;

public interface NotificationProjection {
    Long getNotificationId();
    LocalDateTime getCreateDate();
    String getContent();
    Boolean getReadYn();
}
