package com.didan.social.service;

import com.didan.social.dto.PostDTO;
import com.didan.social.payload.request.CreatePostRequest;
import com.didan.social.payload.request.EditPostRequest;

import java.util.List;

public interface PostService {
    // Tạo 1 bài viết
    String createPost(CreatePostRequest createPostRequest) throws Exception;

    // Lấy tất cả các bài viết
    List<PostDTO> getAllPosts() throws Exception;

    // Lấy tất cả các bài viết theo trang
    List<PostDTO> getAllPostsByPage(int index) throws Exception;

    // Lấy bài viết theo id
    PostDTO getPostById(String postId) throws Exception;

    // Tìm kiếm bài viết theo đề tài
    List<PostDTO> getPostByTitle(String searchName) throws Exception;

    // Like bài viết
    boolean likePost(String postId) throws Exception;

    // Unlike bài viết
    boolean unlikePost(String postId) throws Exception;

    // Cập nhật bài viết
    PostDTO updatePost(String postId, EditPostRequest editPostRequest) throws Exception;

    // Xóa bài viết
    boolean deletePost(String postId) throws Exception;
}
