package com.didan.social.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public class SendMessageRequest {
    @JsonProperty(required = true)
    private String content;
    @JsonProperty(required = false)
    private MultipartFile messageImg = null;

    public SendMessageRequest() {
    }

    public SendMessageRequest(String content, MultipartFile messageImg) {
        this.content = content;
        this.messageImg = messageImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile getMessageImg() {
        return messageImg;
    }

    public void setMessageImg(MultipartFile messageImg) {
        this.messageImg = messageImg;
    }
}
