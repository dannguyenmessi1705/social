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
    Users signup(SignupRequest signupRequest, MultipartFile avatar) throws Exception;

    // Logout
    void logout() throws Exception;
}