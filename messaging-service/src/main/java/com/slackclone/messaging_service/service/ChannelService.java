package com.slackclone.messaging_service.service;

import com.slackclone.messaging_service.dto.ChannelRequest;
import com.slackclone.messaging_service.model.Channel;
import com.slackclone.messaging_service.repository.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {
    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public Channel createChannel(ChannelRequest request, Long userId) {
        Channel channel = new Channel(
                request.getName(),
                request.getDescription(),
                userId
        );
        return channelRepository.save(channel);
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAllByOrderByCreatedAtAsc();
    }

    public Channel getChannelById(Long id){
        return channelRepository.findById(id)
        .orElseThrow(()-> new IllegalArgumentException("Channel with id " + id + " not found"));
    }
}
