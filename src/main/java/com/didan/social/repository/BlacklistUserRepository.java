package com.didan.social.repository;

import com.didan.social.entity.BlacklistUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistUserRepository extends JpaRepository<BlacklistUser, String> {
    BlacklistUser findByUserId(String userId);


}
