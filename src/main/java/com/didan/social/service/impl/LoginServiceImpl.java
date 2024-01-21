package com.didan.social.service.impl;

import com.didan.social.dto.UserDTO;

import java.util.List;

public interface LoginServiceImpl {
    // Lấy tất cả user
    List<UserDTO> getAllUser();

    // Login
    UserDTO login(String email, String password);
}
