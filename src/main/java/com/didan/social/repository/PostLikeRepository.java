package com.didan.social.repository;

import com.didan.social.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikes, String> {
    // Tìm likeid và userid theo postId
    List<PostLikes> findByPosts_PostId(String postId);
    PostLikes findByPosts_PostIdAndUsers_UserId(String postId, String userId);
    boolean existsByPostLikeId_PostIdAndUsers_UserId(String postId, String userId);
    void deleteByPostLikeId_UserIdAndPostLikeId_PostId(String postId, String userId);
}
