package com.didan.social.entity.keys;

import com.didan.social.entity.Comments;
import com.didan.social.entity.Users;
import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class CommentLikeId implements Serializable {
    @Column(name = "comment_id", length = 50, nullable = false)
    private String commentId;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    public CommentLikeId() {
    }

    public CommentLikeId(String commentId, String userId) {
        this.commentId = commentId;
        this.userId = userId;
    }
}
