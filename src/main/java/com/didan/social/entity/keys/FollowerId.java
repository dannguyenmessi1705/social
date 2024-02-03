package com.didan.social.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class FollowerId implements Serializable {
    @Column(name = "follower_id", length = 50, nullable = false)
    private String followerId;

    @Column(name = "followed_id", length = 50, nullable = false)
    private String followedId;

    public FollowerId() {
    }

    public FollowerId(String followerId, String followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

}
