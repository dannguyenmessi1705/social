package com.didan.social.dto;


import java.util.Date;
import java.util.List;

public class UserDTO {
    private String userId;
    private String fullName;
    private String email;
    private String avtUrl;
    private String dateOfBirth;
    private int followers;
    private int followings;
    private int posts;
    private List<String> postId;
    private int participantGroups;

    public UserDTO() {
    }

    public UserDTO(String userId, String fullName, String email, String avtUrl, String dateOfBirth, int followers, int followings, int posts, List<String> postId, int participantGroups) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.avtUrl = avtUrl;
        this.dateOfBirth = dateOfBirth;
        this.followers = followers;
        this.followings = followings;
        this.posts = posts;
        this.postId = postId;
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

    public String getDob() {
        return dateOfBirth;
    }

    public void setDob(String dob) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public List<String> getPostId() {
        return postId;
    }

    public void setPostId(List<String> postId) {
        this.postId = postId;
    }

    public int getParticipantGroups() {
        return participantGroups;
    }

    public void setParticipantGroups(int participantGroups) {
        this.participantGroups = participantGroups;
    }
}
