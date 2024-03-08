package com.didan.social.entity;

import com.didan.social.entity.keys.UserPostId;
import jakarta.persistence.*;

@Entity(name = "user_posts")
public class UserPosts {
    @EmbeddedId
    private UserPostId userPostId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Users users;

    @OneToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Posts posts;

    public UserPosts() {
    }

    public UserPosts(UserPostId userPostId, Users users, Posts posts) {
        this.userPostId = userPostId;
        this.users = users;
        this.posts = posts;
    }

    public UserPostId getUserPostId() {
        return userPostId;
    }

    public void setUserPostId(UserPostId userPostId) {
        this.userPostId = userPostId;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }
}
