package com.slackclone.messaging_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table( name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @Column(name = "sender_id" , nullable = false)
    private Long senderId;

    @Column(name = "sender_name" , nullable = false, length = 50)
    private String senderName;

    @Column(name = "content" , nullable = false, length = 2500)
    private String content;

    @Column(name ="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Message(){}

    public Message(Long channelId, Long senderId, String senderName, String content){
        this.channelId = channelId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getChannelId() {return channelId;}
    public void setChannelId(Long channelId) {this.channelId = channelId;}

    public Long getSenderId() {return senderId;}
    public void setSenderId(Long senderId) {this.senderId = senderId;}

    public String getSenderName() {return senderName;}
    public void setSenderName(String senderName) {this.senderName = senderName;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
