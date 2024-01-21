package com.didan.social.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity(name = "conversations")
public class Conversations {
    @Id
    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "conversation_name", nullable = false, length = 100)
    private String conversationName;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "conversations", cascade = CascadeType.ALL)
    private Set<Messages> messages;

    @OneToMany(mappedBy = "conversations", cascade = CascadeType.ALL)
    private Set<Participants> participants;

    public Conversations() {}

    public Conversations(String conversationId, String conversationName, Date createdAt, Set<Messages> messages, Set<Participants> participants) {
        this.conversationId = conversationId;
        this.conversationName = conversationName;
        this.createdAt = createdAt;
        this.messages = messages;
        this.participants = participants;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Messages> getMessages() {
        return messages;
    }

    public void setMessages(Set<Messages> messages) {
        this.messages = messages;
    }

    public Set<Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participants> participants) {
        this.participants = participants;
    }
}
