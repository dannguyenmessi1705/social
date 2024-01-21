package com.didan.social.entity;

import com.didan.social.entity.keys.UserCommentId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity(name = "user_comment")
public class UserComment {
    @EmbeddedId
    UserCommentId userCommentId;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "post_id", updatable = false, insertable = false)
    private Posts posts;

    @ManyToOne
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private Comments comments;

    public UserComment() {}

    public UserComment(UserCommentId userCommentId, Users users, Posts posts, Comments comments) {
        this.userCommentId = userCommentId;
        this.users = users;
        this.posts = posts;
        this.comments = comments;
    }

    public UserCommentId getUserCommentId() {
        return userCommentId;
    }

    public void setUserCommentId(UserCommentId userCommentId) {
        this.userCommentId = userCommentId;
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

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }
}
