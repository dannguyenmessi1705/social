package com.didan.social.entity;

import jakarta.persistence.*;

@Entity(name = "comment_likes")
public class CommentLikes {
    @Id
    @Column(name = "like_id", nullable = false)
    private String likeId;

    // Khoa ngoai comment_id, user_id
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comments comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    public CommentLikes() {
    }

    public CommentLikes(String likeId, Comments comments, Users users) {
        this.likeId = likeId;
        this.comments = comments;
        this.users = users;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
