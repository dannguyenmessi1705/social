package com.didan.social.service.impl;

import com.didan.social.dto.UserDTO;

import java.util.List;

public interface UserServiceImpl {
    // Lấy tất cả user
    List<UserDTO> getAllUser();
}
