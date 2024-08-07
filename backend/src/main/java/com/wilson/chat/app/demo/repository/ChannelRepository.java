package com.wilson.chat.app.demo.repository;

import com.wilson.chat.app.demo.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByName(String name);
}
