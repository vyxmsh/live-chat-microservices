package com.slackclone.messaging_service.dto;

public class ChannelRequest {
    private String name;
    private String description;
    private Long createdBy;

    public String getName() { return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() { return description;}
    public void setDescription(String description) {this.description = description;}

    public Long getCreatedBy() {return createdBy;}
    public void setCreatedBy(Long createdBy) {this.createdBy = createdBy; }
}