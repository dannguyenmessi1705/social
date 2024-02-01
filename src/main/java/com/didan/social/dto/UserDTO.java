package com.didan.social.dto;


import java.util.Date;

public class UserDTO {
    private String userId;
    private String fullName;
    private String email;
    private String avtUrl;
    private Date dob;
    private int followers;
    private int followeds;
    private int posts;
    private int participantGroups;

    public UserDTO() {
    }

    public UserDTO(String userId, String fullName, String email, String avtUrl, Date dob, int followers, int followeds, int posts, int participantGroups) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.avtUrl = avtUrl;
        this.dob = dob;
        this.followers = followers;
        this.followeds = followeds;
        this.posts = posts;
        this.participantGroups = participantGroups;
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

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFolloweds() {
        return followeds;
    }

    public void setFolloweds(int followeds) {
        this.followeds = followeds;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getParticipantGroups() {
        return participantGroups;
    }

    public void setParticipantGroups(int participantGroups) {
        this.participantGroups = participantGroups;
    }
}
