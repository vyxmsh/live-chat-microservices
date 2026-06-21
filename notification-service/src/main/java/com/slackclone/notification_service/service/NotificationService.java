package com.slackclone.notification_service.service;

import com.slackclone.notification_service.dto.NotificationRequest;
import com.slackclone.notification_service.model.Notification;
import com.slackclone.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List; 

@Service
public class NotificationService {
    
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository)
    {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Notification createNotification(NotificationRequest request){
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setContent(request.getContent());

        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId){
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void markAllRead(Long userId)
    {
        List<Notification> unread = 
                            notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    } 
}
