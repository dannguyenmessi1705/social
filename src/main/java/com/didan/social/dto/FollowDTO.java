package com.didan.social.dto;

import java.util.List;

public class FollowDTO {
    private int quantity;
    private List<String> userId;

    public FollowDTO() {
    }

    public FollowDTO(int quantity, List<String> userId) {
        this.quantity = quantity;
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }
}
