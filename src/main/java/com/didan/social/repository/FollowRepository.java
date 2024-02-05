package com.didan.social.repository;

import com.didan.social.entity.Followers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Followers, String> {
    Followers findFirstByUsers1_UserIdAndUsers2_UserId(String followerId, String followedId);
}
