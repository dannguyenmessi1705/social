package com.didan.social.dto;

public class BlacklistUserDTO {
    private String userId;
    private String fullName;
    private String email;
    private String reportStatus;
    private int reportedQuantity;
    private String blockedAt = null;

    public BlacklistUserDTO() {
    }
    public BlacklistUserDTO(String userId, String fullName, String email, String reportStatus, int reportedQuantity, String blockedAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.reportStatus = reportStatus;
        this.reportedQuantity = reportedQuantity;
        this.blockedAt = blockedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public int getReportedQuantity() {
        return reportedQuantity;
    }

    public void setReportedQuantity(int reportedQuantity) {
        this.reportedQuantity = reportedQuantity;
    }

    public String getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(String blockedAt) {
        this.blockedAt = blockedAt;
    }
}
