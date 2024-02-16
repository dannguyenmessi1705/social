package com.didan.social.dto;


import java.util.Date;

public class ConversationDTO {
    private String conversationId;
    private String conversationName;
    private String createdAt;

    public ConversationDTO() {
    }

    public ConversationDTO(String conversationId, String conversationName, String createdAt) {
        this.conversationId = conversationId;
        this.conversationName = conversationName;
        this.createdAt = createdAt;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
