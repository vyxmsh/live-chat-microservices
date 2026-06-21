package com.slackclone.messaging_service.controller;

import com.slackclone.messaging_service.dto.ChannelRequest;
import com.slackclone.messaging_service.model.Channel;
import com.slackclone.messaging_service.service.ChannelService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService)
    {
        this.channelService = channelService;
    }

    @PostMapping
    public ResponseEntity<Channel> createChannel(@RequestBody ChannelRequest request, @RequestHeader("X-User-Id") Long userId){
        Channel channel = channelService.createChannel(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @GetMapping
    public ResponseEntity<List<Channel>> getAllChannels() {
        return ResponseEntity.ok(channelService.getAllChannels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannel(@PathVariable long id) {
        return ResponseEntity.ok(channelService.getChannelById(id));
    }
    
    
}