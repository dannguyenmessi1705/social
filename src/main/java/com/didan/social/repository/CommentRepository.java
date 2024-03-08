package com.didan.social.repository;

import com.didan.social.entity.Comments;
import com.didan.social.entity.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comments, String> {
    Comments findByCommentId(String commentId);
    @Query("SELECT c.commentId FROM comments c WHERE c.commentId NOT IN (SELECT uc.comments.commentId FROM user_comment uc)")
    List<String> findCommentIdNotInUserComment();
}
