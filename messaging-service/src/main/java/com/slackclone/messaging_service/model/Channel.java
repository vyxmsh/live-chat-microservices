package com.slackclone.messaging_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;



@Entity
@Table(name = "channels")   
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Channel() {}

    public Channel(String name, String description, Long createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName () { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() {return description; }
    public void setDescription(String description) {this.description = description;}

    public Long getCreatedBy () { return createdBy;}
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy;}

    public LocalDateTime getCreatedAt () { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;} 
}
