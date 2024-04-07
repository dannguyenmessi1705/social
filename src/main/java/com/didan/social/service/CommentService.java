package com.didan.social.service;

import com.didan.social.dto.CommentDTO;
import com.didan.social.payload.request.CreateCommentRequest;
import com.didan.social.payload.request.EditCommentRequest;

import java.util.List;

public interface CommentService {
    // Lấy ra tất cả comment của 1 bài viết
    List<CommentDTO> getCommentsInPost(String postId) throws Exception;
    // Bình luận vào 1 bài viết
    String postCommentInPost(String postId, CreateCommentRequest createCommentRequest) throws Exception;
    // Lấy ra 1 comment theo id
    CommentDTO getCommentById(String commentId) throws Exception;
    // Like comment
    boolean likeComment(String commentId) throws Exception;
    // Unlike comment
    boolean unlikeComment(String commentId) throws Exception;
    // Chỉnh sửa comment
    CommentDTO updateComment(String commentId, EditCommentRequest editCommentRequest) throws Exception;
    // Xóa comment
    boolean deleteComment(String commentId) throws Exception;
}
