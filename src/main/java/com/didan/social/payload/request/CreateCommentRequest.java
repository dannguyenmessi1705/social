package com.didan.social.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public class CreateCommentRequest {
    @JsonProperty(required = true)
    private String content;
    @JsonProperty(required = false)
    private MultipartFile commentImg = null;

    public CreateCommentRequest() {
    }

    public CreateCommentRequest(String content, MultipartFile commentImg) {
        this.content = content;
        this.commentImg = commentImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile getCommentImg() {
        return commentImg;
    }

    public void setCommentImg(MultipartFile commentImg) {
        this.commentImg = commentImg;
    }
}
