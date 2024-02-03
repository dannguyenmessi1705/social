package com.didan.social.service.impl;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.Users;
import com.didan.social.payload.request.SignupRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AuthServiceImpl {

    // Login
    Users login(String email, String password) throws Exception;

    // Signup
    Users signup(SignupRequest signupRequest) throws Exception;

    // Logout
    void logout() throws Exception;

    // Request Forgot Password
    String requestForgot(String email) throws Exception;

    // Verify Token Reset Password
    String verifyToken(String token) throws Exception;

    // Update Password
    boolean updatePassword(String token, String newPassword) throws Exception;
}