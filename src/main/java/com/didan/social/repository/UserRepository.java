package com.didan.social.repository;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    // Ngoài các method mặc định có sẵn, ta có thể định nghĩa thêm method mới dựa theo nguyên tắc đặt tên của JPA
    // Get All User
    // Login
    Users findFirstByEmail(String email);

    // Find By Token Reset Password
    Users findFirstByResetToken(String token);

    // Find User By userid
    Users findFirstByUserId(String userId);

    // Search User By Fullname
    List<Users> findByFullNameContainingOrEmailLike(String name, String email);
}
