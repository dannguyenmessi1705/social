package com.didan.social.service.impl;

import com.didan.social.dto.UserDTO;
import com.didan.social.payload.request.SignupRequest;

import java.util.List;

public interface AuthServiceImpl {
    // Lấy tất cả user
    List<UserDTO> getAllUser();

    // Login
    UserDTO login(String email, String password) throws Exception;

    // Signup
    UserDTO signup(SignupRequest signupRequest) throws Exception;

    // Logout
    void logout() throws Exception;
}