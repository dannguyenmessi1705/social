package com.didan.social.repository;

import com.didan.social.entity.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikes, String> {
    List<CommentLikes> findAllByComments_CommentId(String commentId);
    CommentLikes findFirstByUsers_UserIdAndComments_CommentId(String userId, String commentId);
}
