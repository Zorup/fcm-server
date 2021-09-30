package com.zorup.fcm.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<NotificationProjection> findByReceiverIdAndEventTypeIsTrue(Long receiverId);
}
