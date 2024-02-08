package com.didan.social.repository;

import com.didan.social.entity.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversations, String> {
    Conversations findFirstByConversationId(String conversationId);
    List<Conversations> findAllByConversationNameContainingOrderByCreatedAtDesc(String conversationName);
}
