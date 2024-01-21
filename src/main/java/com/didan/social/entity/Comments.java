package com.didan.social.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity(name = "comments")
public class Comments {
    @Id
    @Column(name = "comment_id")
    private String commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "comment_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentAt;

    @OneToMany(mappedBy = "comments", cascade = CascadeType.ALL)
    private Set<UserComment> userComments;

    @OneToMany(mappedBy = "comments", cascade = CascadeType.ALL)
    private Set<CommentLikes> commentLikes;


    public Comments() {}

    public Comments(String commentId, String content, Date commentAt, Set<UserComment> userComments, Set<CommentLikes> commentLikes) {
        this.commentId = commentId;
        this.content = content;
        this.commentAt = commentAt;
        this.userComments = userComments;
        this.commentLikes = commentLikes;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentAt() {
        return commentAt;
    }

    public void setCommentAt(Date commentAt) {
        this.commentAt = commentAt;
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
