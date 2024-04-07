package com.didan.social.service;

import com.didan.social.dto.BlacklistUserDTO;

import java.util.List;

public interface AdminService {
    // Phân quyền cho user làm admin
    boolean grantAdmin(String userId) throws Exception;
    List<BlacklistUserDTO> getAllBlacklistUser() throws Exception;
    boolean blockUser(String userId) throws Exception;
    boolean unblockUser(String userId) throws Exception;
}
