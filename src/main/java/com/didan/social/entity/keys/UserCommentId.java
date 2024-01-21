package com.didan.social.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserCommentId implements Serializable {
    @Column(name = "post_id", nullable = false, length = 50)
    private String postId;

    @Column(name = "comment_id", nullable = false, length = 50)
    private String commentId;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    public UserCommentId() {
    }

    public UserCommentId(String postId, String commentId, String userId) {
        this.postId = postId;
        this.commentId = commentId;
        this.userId = userId;
    }
}
