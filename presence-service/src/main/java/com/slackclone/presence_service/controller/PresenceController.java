package com.slackclone.presence_service.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.Duration;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/presence")
public class PresenceController {
    private final StringRedisTemplate redis;
    private static final Duration TTL = Duration.ofSeconds(45);
    private static final String PREFIX = "presence:";

    public PresenceController(StringRedisTemplate redis){
        this.redis = redis;
    }

    //called by front-end every 30s - gateway forwards X-User-Id header

    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(@RequestHeader("X-User-Id") String userId){
        redis.opsForValue().set(PREFIX + userId, "online",TTL);

        return ResponseEntity.ok().build();
    }

    //check if specific user is Online
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String,Object>> getPresence(@PathVariable String userId) {
        Boolean exists = redis.hasKey(PREFIX + userId);
        return ResponseEntity.ok(Map.of(
                                "userId", userId,
                                "online", Boolean.TRUE.equals(exists)
        ));
    }

    //Explicit logout - remove key immediately
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> goOffline(@PathVariable String userId)
    {
        redis.delete(PREFIX + userId);
        return ResponseEntity.ok().build();
    }
    
    
}