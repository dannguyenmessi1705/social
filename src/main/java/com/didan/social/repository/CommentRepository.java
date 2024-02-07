package com.didan.social.repository;

import com.didan.social.entity.Comments;
import com.didan.social.entity.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comments, String> {
    Comments findByCommentId(String commentId);
}
