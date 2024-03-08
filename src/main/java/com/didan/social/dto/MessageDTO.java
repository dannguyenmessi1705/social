package com.didan.social.dto;

public class MessageDTO {
    private String messageId;
    private String content;
    private String messageImg;
    private String sentAt;
    private String conversationId;
    private String senderId;

    public MessageDTO() {
    }

    public MessageDTO(String messageId, String content, String messageImg, String sentAt, String conversationId, String senderId) {
        this.messageId = messageId;
        this.content = content;
        this.messageImg = messageImg;
        this.sentAt = sentAt;
        this.conversationId = conversationId;
        this.senderId = senderId;
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

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
