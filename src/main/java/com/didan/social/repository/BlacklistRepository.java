package com.didan.social.repository;

import com.didan.social.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistToken, String> {
    // Custom
    BlacklistToken findFirstByToken(String token);
}
