package com.didan.social.repository;

import com.didan.social.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, String> {
    // Tìm post theo id
    Posts findFirstByPostId(String postId);

    List<Posts> findByTitleOrBodyContainingOrderByPostedAtDesc(String title, String body);
}
