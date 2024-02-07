package com.didan.social.repository;

import com.didan.social.entity.Posts;
import com.didan.social.entity.UserPosts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPostRepository extends JpaRepository<UserPosts, String> {
    UserPosts findFirstByPosts_PostIdAndUsers_UserId(String postId, String userId);
    UserPosts findFirstByPosts(Posts posts);
}
