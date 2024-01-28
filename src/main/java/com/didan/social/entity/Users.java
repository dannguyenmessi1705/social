package com.didan.social.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity(name = "users")
public class Users {
    @Id
    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "profile_avatar", nullable = false, length = 255)
    private String avtUrl;

    @Column(name = "date_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "reset_password_expires", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetExp;

    @Column(name = "reset_password_token", nullable = true, length = 255)
    private String resetToken;

    @OneToMany(mappedBy = "users1", cascade = CascadeType.ALL)
    private Set<Followers> followers;

    @OneToMany(mappedBy = "users2", cascade = CascadeType.ALL)
    private Set<Followers> followeds;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<PostLikes> postLikes;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<UserPosts> userPosts;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<Participants> participants;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<Messages> messages;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<UserComment> userComments;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<CommentLikes> commentLikes;

    public Users() {}

    public Users(String userId, String fullName, String email, String password, String avtUrl, Date dob, Date resetExp, String resetToken, Set<Followers> followers, Set<Followers> followeds, Set<PostLikes> postLikes, Set<UserPosts> userPosts, Set<Participants> participants, Set<Messages> messages, Set<UserComment> userComments, Set<CommentLikes> commentLikes) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.avtUrl = avtUrl;
        this.dob = dob;
        this.resetExp = resetExp;
        this.resetToken = resetToken;
        this.followers = followers;
        this.followeds = followeds;
        this.postLikes = postLikes;
        this.userPosts = userPosts;
        this.participants = participants;
        this.messages = messages;
        this.userComments = userComments;
        this.commentLikes = commentLikes;
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

    public Set<Followers> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Followers> followers) {
        this.followers = followers;
    }

    public Set<Followers> getFolloweds() {
        return followeds;
    }

    public void setFolloweds(Set<Followers> followeds) {
        this.followeds = followeds;
    }

    public Set<PostLikes> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(Set<PostLikes> postLikes) {
        this.postLikes = postLikes;
    }

    public Set<UserPosts> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(Set<UserPosts> userPosts) {
        this.userPosts = userPosts;
    }

    public Set<Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participants> participants) {
        this.participants = participants;
    }

    public Set<Messages> getMessages() {
        return messages;
    }

    public void setMessages(Set<Messages> messages) {
        this.messages = messages;
    }

    public Set<UserComment> getUserComments() {
        return userComments;
    }

    public void setUserComments(Set<UserComment> userComments) {
        this.userComments = userComments;
    }

    public Set<CommentLikes> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(Set<CommentLikes> commentLikes) {
        this.commentLikes = commentLikes;
    }
}
