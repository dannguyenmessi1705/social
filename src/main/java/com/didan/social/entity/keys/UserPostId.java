package com.didan.social.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserPostId implements Serializable {
    @Column(name = "post_id")
    private String postId;

    @Column(name = "user_id")
    private String userId;

    public UserPostId() {
    }

    public UserPostId(String postId, String userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
