package com.slackclone.messaging_service.repository;

import com.slackclone.messaging_service.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
        List<Channel> findAllByOrderByCreatedAtAsc();
}
