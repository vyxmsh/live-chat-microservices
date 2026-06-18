package com.slackclone.messaging_service.service;

import com.slackclone.messaging_service.dto.ChatMessage;
import com.slackclone.messaging_service.model.Message;
import com.slackclone.messaging_service.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(ChatMessage chatMessage) {
        Message message = new Message(
                chatMessage.getChannelId(),
                chatMessage.getSenderId(),
                chatMessage.getSenderName(),
                chatMessage.getContent()
        );
        return messageRepository.save(message); 
    }

    public Map<String,Object> getMessages(Long channelId, int page){
        Pageable pageable = PageRequest.of(page,20);
        Page<Message> result = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);
        
        Map<String,Object> response = new HashMap<>();
        response.put("messages",result.getContent());
        response.put("currentPage", result.getNumber());
        response.put("totalPages", result.getTotalPages());
        response.put("hasMore", result.hasNext());
        return response;
    }
    
}
