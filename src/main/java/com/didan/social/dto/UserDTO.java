package com.didan.social.dto;


import java.util.Date;

public class UserDTO {
    private String userId;
    private String fullName;
    private String email;
    private String password;
    private String avtUrl;
    private Date dob;
    private Date resetExp;
    private String resetToken;
    private String refreshToken;

    public UserDTO() {
    }

    public UserDTO(String userId, String fullName, String email, String password, String avtUrl, Date dob, Date resetExp, String resetToken, String refreshToken) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.avtUrl = avtUrl;
        this.dob = dob;
        this.resetExp = resetExp;
        this.resetToken = resetToken;
        this.refreshToken = refreshToken;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getResetExp() {
        return resetExp;
    }

    public void setResetExp(Date resetExp) {
        this.resetExp = resetExp;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
