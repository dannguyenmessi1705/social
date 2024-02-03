package com.didan.social.repository;

import com.didan.social.entity.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, String> {
    // Tìm theo postId
    List<UserComment> findByPosts_PostId(String postId);
}
