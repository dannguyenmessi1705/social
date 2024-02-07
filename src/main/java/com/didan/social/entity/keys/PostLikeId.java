package com.didan.social.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PostLikeId implements Serializable {
    @Column(name = "post_id", nullable = false, length = 50)
    private String postId;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    public PostLikeId() {
    }

    public PostLikeId(String postId, String userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
