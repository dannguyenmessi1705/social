package com.didan.social.service.impl;

import com.didan.social.dto.UserDTO;

import java.util.List;

public interface UserServiceImpl {
    // Lấy tất cả user
    List<UserDTO> getAllUser();
    // Lấy user từ id
    UserDTO getUserById(String userId);
    //Tìm kiếm
    List<UserDTO> searchUser(String searchName) throws Exception;
}
