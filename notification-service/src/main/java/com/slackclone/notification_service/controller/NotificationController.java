package com.slackclone.notification_service.controller;

import com.slackclone.notification_service.dto.NotificationRequest;
import com.slackclone.notification_service.model.Notification;
import com.slackclone.notification_service.service.NotificationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/notifications")
public class NotificationController{
    
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    //called internally by messaging-service (via RestTemplate, not through gateway)
    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody NotificationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.createNotification(request));
    }

    //Called by frontend through gateway -  userId from Header
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUnread(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }
    
    //Marked all as read
    @PutMapping("/{userId}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long userId) {
        notificationService.markAllRead(userId);
        return ResponseEntity.ok().build();
    }    
}