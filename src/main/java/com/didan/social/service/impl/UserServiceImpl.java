package com.didan.social.service.impl;

import com.didan.social.dto.UserDTO;
import com.didan.social.payload.request.EditUserRequest;

import java.util.List;

public interface UserServiceImpl {
    // Lấy tất cả user
    List<UserDTO> getAllUser();
    // Lấy user từ id
    UserDTO getUserById(String userId);
    //Tìm kiếm
    List<UserDTO> searchUser(String searchName) throws Exception;

    // Sửa thông tin user
    boolean updateUser(EditUserRequest editUserRequest) throws Exception;

    // Phân quyền cho user làm admin
    boolean grantAdmin(String userId) throws Exception;
}
