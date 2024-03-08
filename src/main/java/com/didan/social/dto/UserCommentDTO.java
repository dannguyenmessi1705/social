package com.didan.social.dto;


public class UserCommentDTO {
    private String userId;
    private String commentId;

    public UserCommentDTO() {
    }

    public UserCommentDTO(String userId, String commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

}
