package com.didan.social.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "blacklist_token")
public class BlacklistToken {
    @Id
    @Column(name = "token", nullable = true, length = 255)
    private String token;

    public BlacklistToken() {
    }

    public BlacklistToken(String token) {
        this.token = token;
    }
}
