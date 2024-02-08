package com.didan.social.repository;

import com.didan.social.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Messages, String> {
    List<Messages> findAllByConversations_ConversationIdOrderBySentAt(String conversationId);
}
