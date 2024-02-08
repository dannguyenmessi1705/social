package com.didan.social.entity;

import com.didan.social.entity.keys.ParticipantId;
import jakarta.persistence.*;

@Entity(name = "participants")
public class Participants {
    @EmbeddedId
    private ParticipantId participantId;

    @ManyToOne
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false)
    private Conversations conversations;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Users users;

    public Participants() {}

    public Participants(ParticipantId participantId, Conversations conversations, Users users) {
        this.participantId = participantId;
        this.conversations = conversations;
        this.users = users;
    }

    public ParticipantId getParticipantId() {
        return participantId;
    }

    public void setParticipantId(ParticipantId participantId) {
        this.participantId = participantId;
    }

    public Conversations getConversations() {
        return conversations;
    }

    public void setConversations(Conversations conversations) {
        this.conversations = conversations;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
