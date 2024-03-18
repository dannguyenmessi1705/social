package com.didan.social.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "blacklist_user")
public class BlacklistUser {
    @Id
    @Column(name = "user_id")
    private String userId;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(name = "status")
    private String status = "pending";

    @Column(name = "reported_quantity")
    private int reportedQuantity = 0;

    @Column(name = "blocked_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date blockedAt = null;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReportedQuantity() {
        return reportedQuantity;
    }

    public void setReportedQuantity(int reportedQuantity) {
        this.reportedQuantity = reportedQuantity;
    }

    public Date getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(Date blockedAt) {
        this.blockedAt = blockedAt;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
