package com.didan.social.dto;

import com.didan.social.entity.Comments;
import com.didan.social.entity.Posts;
import com.didan.social.entity.Users;
import com.didan.social.entity.keys.UserCommentId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
