package com.slackclone.notification_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String type; //NEW-MESSAGE, mention

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean isRead = false;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    //getters and setters

    public Long getId() { return id;}
    public Long getUserId() {return userId; }
    public void setUserId(Long userId) {this.userId = userId;}

    public String getType() { return type; }
    public void setType(String type) { this.type = type;}
    
    public String getContent() { return content;}
    public void setContent(String content) {this.content = content;}

    public Boolean isRead() {return isRead;}
    public void setRead(boolean read) {isRead = read;}

    public LocalDateTime getCreatedAt() {return createdAt;}
}
