package com.didan.social.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "messages")
public class Messages {
    @Id
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "messageImg", nullable = true)
    private String messageImg;

    @Column(name = "sent_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversations conversations;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Users users;

    public Messages() {}

    public Messages(String messageId, String content, String messageImg, Date sentAt, Conversations conversations, Users users) {
        this.messageId = messageId;
        this.content = content;
        this.messageImg = messageImg;
        this.sentAt = sentAt;
        this.conversations = conversations;
        this.users = users;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageImg() {
        return messageImg;
    }

    public void setMessageImg(String messageImg) {
        this.messageImg = messageImg;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
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
