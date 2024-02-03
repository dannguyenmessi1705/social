package com.didan.social.entity;

import com.didan.social.entity.keys.PostLikeId;
import jakarta.persistence.*;

@Entity(name = "post_likes")
public class PostLikes {
    @EmbeddedId
    PostLikeId postLikeId;

    @ManyToOne
    @JoinColumn(name = "post_id", updatable = false, insertable = false)
    private Posts posts;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private Users users;

    public PostLikes() {
    }

    public PostLikes(PostLikeId postLikeId, Posts posts, Users users) {
        this.postLikeId = postLikeId;
        this.posts = posts;
        this.users = users;
    }

    public PostLikeId getPostLikeId() {
        return postLikeId;
    }

    public void setPostLikeId(PostLikeId postLikeId) {
        this.postLikeId = postLikeId;
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
