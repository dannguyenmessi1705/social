package com.didan.social.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ParticipantId implements Serializable {
    @Column(name = "conversation_id", nullable = false, length = 50)
    private String conversationId;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    public ParticipantId() {
    }

    public ParticipantId(String conversationId, String userId) {
        this.conversationId = conversationId;
        this.userId = userId;
    }

}
