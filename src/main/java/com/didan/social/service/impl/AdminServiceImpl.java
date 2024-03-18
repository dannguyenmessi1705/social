package com.didan.social.service.impl;

import com.didan.social.dto.BlacklistUserDTO;
import com.didan.social.entity.BlacklistUser;

import java.util.List;

public interface AdminServiceImpl {
    // Phân quyền cho user làm admin
    boolean grantAdmin(String userId) throws Exception;
    List<BlacklistUserDTO> getAllBlacklistUser() throws Exception;
    boolean blockUser(String userId) throws Exception;
    boolean unblockUser(String userId) throws Exception;
}
