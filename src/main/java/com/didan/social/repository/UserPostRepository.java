package com.didan.social.repository;

import com.didan.social.entity.UserPosts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPosts, String> {
}
