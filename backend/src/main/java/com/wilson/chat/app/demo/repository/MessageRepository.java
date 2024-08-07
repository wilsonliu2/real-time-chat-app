package com.wilson.chat.app.demo.repository;

import com.wilson.chat.app.demo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChannelId(Long channelId);
}
