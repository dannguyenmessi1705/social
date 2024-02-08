package com.didan.social.repository;

import com.didan.social.entity.Participants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participants, String> {
    Participants findFirstByConversations_ConversationIdAndUsers_UserId(String conversationId, String userId);
    List<Participants> findAllByUsers_UserId(String userId);
}
