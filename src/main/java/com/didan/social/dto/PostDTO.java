package com.didan.social.dto;

import com.didan.social.entity.PostLikes;
import com.didan.social.entity.UserComment;
import com.didan.social.entity.UserPosts;

import java.util.Date;
import java.util.List;

public class PostDTO {
    private UserPostDTO userPosts;
    private String title;
    private String postImg;
    private String body;
    private Date postedAt;
    private int likesQuantity;
    private List<PostLikeDTO> postLikes;
    private int commentsQuantity;
    private List<UserCommentDTO> userComments;

    public PostDTO() {
    }

    public PostDTO(UserPostDTO userPosts, String title, String postImg, String body, Date postedAt, int likesQuantity, List<PostLikeDTO> postLikes, int commentsQuantity, List<UserCommentDTO> userComments) {
        this.userPosts = userPosts;
        this.title = title;
        this.postImg = postImg;
        this.body = body;
        this.postedAt = postedAt;
        this.likesQuantity = likesQuantity;
        this.postLikes = postLikes;
        this.commentsQuantity = commentsQuantity;
        this.userComments = userComments;
    }

    public UserPostDTO getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(UserPostDTO userPosts) {
        this.userPosts = userPosts;
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

    public int getLikesQuantity() {
        return likesQuantity;
    }

    public void setLikesQuantity(int likesQuantity) {
        this.likesQuantity = likesQuantity;
    }

    public List<PostLikeDTO> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(List<PostLikeDTO> postLikes) {
        this.postLikes = postLikes;
    }

    public int getCommentsQuantity() {
        return commentsQuantity;
    }

    public void setCommentsQuantity(int commentsQuantity) {
        this.commentsQuantity = commentsQuantity;
    }

    public List<UserCommentDTO> getUserComment() {
        return userComments;
    }

    public void setUserComment(List<UserCommentDTO> userComment) {
        this.userComments = userComment;
    }
}
