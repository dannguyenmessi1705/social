package com.didan.social.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity(name = "posts")
public class Posts {
    @Id
    @Column(name = "post_id")
    private String postId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "post_img", nullable = true, length = 255)
    private String postImg;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "posted_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedAt;

    @OneToOne(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserPosts userPost;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLikes> postLikes;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserComment> userComments;

    public Posts() {}

    public Posts(String postId, String title, String postImg, String body, Date postedAt, UserPosts userPost, Set<PostLikes> postLikes, Set<UserComment> userComments) {
        this.postId = postId;
        this.title = title;
        this.postImg = postImg;
        this.body = body;
        this.postedAt = postedAt;
        this.userPost = userPost;
        this.postLikes = postLikes;
        this.userComments = userComments;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public UserPosts getUserPost() {
        return userPost;
    }

    public void setUserPos(UserPosts userPost) {
        this.userPost = userPost;
    }

    public Set<PostLikes> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(Set<PostLikes> postLikes) {
        this.postLikes = postLikes;
    }

    public Set<UserComment> getUserComments() {
        return userComments;
    }

    public void setUserComments(Set<UserComment> userComments) {
        this.userComments = userComments;
    }

    @Overide
    public String toString(){
        return post_id + " " + title + " " + body;
    }
}
