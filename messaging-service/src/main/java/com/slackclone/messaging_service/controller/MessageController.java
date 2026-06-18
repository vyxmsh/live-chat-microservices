package com.slackclone.messaging_service.controller;

import com.slackclone.messaging_service.dto.ChatMessage;
import com.slackclone.messaging_service.model.Message;
import com.slackclone.messaging_service.service.MessageService;
import org.springframework.http.ResponseEntity;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class MessageController {
    
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageService messageService, 
        SimpMessagingTemplate messagingTemplate)
    {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage chatMessage)
    {
        Message saved = messageService.saveMessage(chatMessage);
        messagingTemplate.convertAndSend("/topic/channel." + saved.getChannelId(), saved);
    }

    @GetMapping("/api/channels/{channelId}/messages")
    public ResponseEntity<?> getMessages(
                    @PathVariable Long channelId, 
                    @RequestParam(defaultValue = "0" ) int page){
            return ResponseEntity.ok(messageService.getMessages(channelId,page));
    }
}
