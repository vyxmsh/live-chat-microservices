package com.slackclone.messaging_service.dto;

public class ChatMessage {
    private Long channelId;
    private Long senderId;
    private String content;
    private String senderName;

    public ChatMessage() {}

    public Long getChannelId() { return channelId;}
    public void setChannelId(Long channelId) { this.channelId = channelId;}

    public Long getSenderId() { return senderId;}
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName;}
    public void setSenderName(String senderName) {this.senderName = senderName;}

    public String getContent() { return content;}
    public void setContent(String content) {this.content = content;}
}