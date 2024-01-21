package com.didan.social.entity;

import jakarta.persistence.*;

@Entity(name = "post_likes")
public class PostLikes {
    @Id
    @Column(name = "like_id")
    private String likeId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts posts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    public PostLikes() {
    }

    public PostLikes(String likeId, Posts posts, Users users) {
        this.likeId = likeId;
        this.posts = posts;
        this.users = users;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
