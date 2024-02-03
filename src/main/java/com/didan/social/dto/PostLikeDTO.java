package com.didan.social.dto;

public class PostLikeDTO {
    private String userId;

    public PostLikeDTO() {

    }

    public PostLikeDTO(String userId) {
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
