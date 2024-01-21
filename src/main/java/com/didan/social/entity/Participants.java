package com.didan.social.entity;

import jakarta.persistence.*;

@Entity(name = "participants")
public class Participants {
    @Id
    @Column(name = "participant_id")
    private String participantId;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversations conversations;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    public Participants() {}

    public Participants(String participantId, Conversations conversations, Users users) {
        this.participantId = participantId;
        this.conversations = conversations;
        this.users = users;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
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
