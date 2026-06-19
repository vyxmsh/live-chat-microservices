package com.slackclone.messaging_service.service;

import com.slackclone.messaging_service.dto.ChatMessage;
import com.slackclone.messaging_service.model.Message;
import com.slackclone.messaging_service.repository.MessageRepository;


import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final RestTemplate restTemplate;

    public MessageService(MessageRepository messageRepository, RestTemplate restTemplate) {
        this.messageRepository = messageRepository;
        this.restTemplate = restTemplate;
    }
    @Transactional
    public Message saveMessage(ChatMessage chatMessage) {
        Message message = new Message();

        message.setChannelId(chatMessage.getChannelId());
        message.setSenderId(chatMessage.getSenderId());
        message.setSenderName(chatMessage.getSenderName());
        message.setContent(chatMessage.getContent());
            
        Message saved = messageRepository.save(message);
        
        //Notify notifications-service (fire and forget - don't fail if it's down)

        try{
            Map<String, Object> notification = new HashMap<>();
            notification.put("userId",chatMessage.getSenderId());
            notification.put("type","new_message");
            notification.put("content", chatMessage.getSenderName() + ":" + chatMessage.getContent());
            restTemplate.postForEntity("http://NOTIFICATION-SERVICE/api/notifications", notification, Void.class);  

        }
        catch(Exception e){
            System.err.println("Failed to send notification:" + e.getMessage());
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Message> getMessages(Long channelId, int page)
    {
        Page<Message> messagePage = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, PageRequest.of(page,20));

        return messagePage.getContent();
    }
    
}
