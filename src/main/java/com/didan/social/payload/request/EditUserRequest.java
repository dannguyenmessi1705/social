package com.didan.social.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public class EditUserRequest {
    @JsonProperty(required = false)
    private String email = null;
    @JsonProperty(required = true)
    private String password = null;
    @JsonProperty(required = false)
    private String newPassword = null;
    @JsonProperty(required = false)
    private MultipartFile avatar = null;

    public EditUserRequest() {
    }

    public EditUserRequest(String email, String password, String newPassword, MultipartFile avatar) {
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
        this.avatar = avatar;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }
}
