package com.didan.social.dto;


import java.util.Date;

public class UserDTO {
    private String userId;
    private String fullName;
    private String email;
    private String avtUrl;
    private Date dob;

    public UserDTO() {
    }

    public UserDTO(String userId, String fullName, String email, String avtUrl, Date dob) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.avtUrl = avtUrl;
        this.dob = dob;
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

    public String getAvtUrl() {
        return avtUrl;
    }

    public void setAvtUrl(String avtUrl) {
        this.avtUrl = avtUrl;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

}
