package com.didan.social.service;

import com.didan.social.dto.BlacklistUserDTO;
import com.didan.social.entity.BlacklistToken;
import com.didan.social.entity.BlacklistUser;
import com.didan.social.entity.Users;
import com.didan.social.repository.BlacklistRepository;
import com.didan.social.repository.BlacklistUserRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.AdminServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AdminService implements AdminServiceImpl {
    private final AuthorizePathService authorizePathService;
    private final UserRepository userRepository;
    private final BlacklistUserRepository blacklistUserRepository;
    private final BlacklistRepository blacklistTokenRepository;
    private final Logger logger = LoggerFactory.getLogger(AdminService.class);
    @Autowired
    public AdminService(AuthorizePathService authorizePathService, UserRepository userRepository, BlacklistUserRepository blacklistUserRepository, BlacklistRepository blacklistTokenRepository) {
        this.authorizePathService = authorizePathService;
        this.userRepository = userRepository;
        this.blacklistUserRepository = blacklistUserRepository;
        this.blacklistTokenRepository = blacklistTokenRepository;
    }
    @Override
    public boolean grantAdmin(String userIdGranted) throws Exception {
        Users user = authAdmin();
        Users user_grant = userRepository.findFirstByUserId(userIdGranted);
        if (user_grant == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        if (user_grant.getIsAdmin() == 1){
            logger.error("User is already admin");
            throw new Exception("User is already admin");
        }
        user_grant.setIsAdmin(1);
        userRepository.save(user_grant);
        return true;
    }

    @Override
    public List<BlacklistUserDTO> getAllBlacklistUser() throws Exception {
        Users user = authAdmin();
        List<BlacklistUser> blacklistUsers = blacklistUserRepository.findAll();
        if (blacklistUsers.isEmpty()) {
            logger.info("No one in blacklist");
            return Collections.emptyList();
        }
        List<BlacklistUserDTO> blacklists = new ArrayList<>();
        for (BlacklistUser blacklistUser : blacklistUsers){
            BlacklistUserDTO blacklistUserDTO = new BlacklistUserDTO();
            blacklistUserDTO.setUserId(blacklistUser.getUserId());
            blacklistUserDTO.setFullName(blacklistUser.getUsers().getFullName());
            blacklistUserDTO.setEmail(blacklistUser.getUsers().getEmail());
            blacklistUserDTO.setReportStatus(blacklistUser.getStatus());
            blacklistUserDTO.setReportedQuantity(blacklistUser.getReportedQuantity());
            if (blacklistUser.getBlockedAt() != null){
                blacklistUserDTO.setBlockedAt(blacklistUser.getBlockedAt().toString());
            }
            blacklists.add(blacklistUserDTO);
        }
        return blacklists;
    }

    @Override
    public boolean blockUser(String userId) throws Exception {
        Users user = authAdmin();
        Users user_block = userRepository.findFirstByUserId(userId);
        if (user_block == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        if (StringUtils.hasText(user_block.getAccessToken())){
            blacklistTokenRepository.save(new BlacklistToken(user_block.getAccessToken()));
        }
        BlacklistUser blacklistUser = blacklistUserRepository.findByUserId(userId);
        if (blacklistUser != null){
            if (blacklistUser.getStatus().equals("blocked")){
                logger.error("User is already in blacklist");
                throw new Exception("User is already in blacklist");
            } else {
                blacklistUser.setStatus("blocked");
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                Date nowSql = Timestamp.valueOf(now);
                blacklistUser.setBlockedAt(nowSql);
                System.out.println(1);
                blacklistUserRepository.save(blacklistUser);
                return true;
            }
        } else {
            BlacklistUser newUser = new BlacklistUser();
            newUser.setUserId(userId);
            newUser.setStatus("blocked");
            newUser.setReportedQuantity(1);
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            Date nowSql = Timestamp.valueOf(now);
            newUser.setBlockedAt(nowSql);
            blacklistUserRepository.save(newUser);
            return true;
        }
    }

    @Override
    public boolean unblockUser(String userId) throws Exception {
        Users user = authAdmin();
        Users user_unblock = userRepository.findFirstByUserId(userId);
        if (user_unblock == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        BlacklistUser blacklistUser = blacklistUserRepository.findByUserId(userId);
        if (blacklistUser == null){
            logger.error("User is not in blacklist");
            throw new Exception("User is not in blacklist");
        } else {
            if (blacklistUser.getStatus().equals("blocked")){
                blacklistUser.setStatus("pending");
                blacklistUser.setBlockedAt(null);
                blacklistUser.setReportedQuantity(0);
                blacklistUserRepository.save(blacklistUser);
                return true;
            } else {
                logger.error("User has not been blocked");
                throw new Exception("User has not been blocked");
            }
        }
    }

    public Users authAdmin() throws Exception{
        String userId = authorizePathService.getUserIdAuthoried();
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        if (user.getIsAdmin() == 0){
            logger.error("You are not admin");
            throw new Exception("You are not admin");
        }
        return user;
    }
}
