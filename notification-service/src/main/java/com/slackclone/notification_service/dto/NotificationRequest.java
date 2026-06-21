package com.slackclone.notification_service.dto;

public class NotificationRequest {
    
    private Long userId;
    private String type;
    private String content;

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

}
