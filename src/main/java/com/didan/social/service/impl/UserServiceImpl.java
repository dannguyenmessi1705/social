package com.didan.social.service.impl;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.BlacklistUser;
import com.didan.social.entity.UserPosts;
import com.didan.social.entity.Users;
import com.didan.social.payload.request.EditUserRequest;
import com.didan.social.repository.BlacklistUserRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.AuthorizePathService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserServiceImpl implements com.didan.social.service.UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final AuthorizePathService authorizePathService;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadsServiceImpl fileUploadsService;
    private final BlacklistUserRepository blacklistUserRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AuthorizePathService authorizePathService,
                           PasswordEncoder passwordEncoder,
                           FileUploadsServiceImpl fileUploadsService,
                           BlacklistUserRepository blacklistUserRepository
    ){
        this.userRepository = userRepository;
        this.authorizePathService = authorizePathService;
        this.passwordEncoder = passwordEncoder;
        this.fileUploadsService = fileUploadsService;
        this.blacklistUserRepository = blacklistUserRepository;
    }
    @Override
    public List<UserDTO> getAllUser(){
        List<Users> users = userRepository.findAll();
        if (users.size() <= 0) {
            logger.info("No one");
            return Collections.emptyList();
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        for (Users user : users){
            UserDTO userDTO = convertToUserDTO(user);
            userDTOS.add(userDTO);
        }
        userDTOS.sort(Comparator.comparingInt(UserDTO::getFollowers).reversed());
        return userDTOS;
    }

    @Override
    public UserDTO getUserById(String userId) {
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.info("No user to found");
            return null;
        }
        else {
            return convertToUserDTO(user);
        }
    }

    @Override
    public List<UserDTO> searchUser(String searchName) throws Exception {
        List<Users> users = userRepository.findByFullNameContainingOrEmailLike(searchName, searchName);
        if (users.isEmpty()) {
            logger.info("No one");
            return Collections.emptyList();
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        for (Users user : users){
            UserDTO userDTO = convertToUserDTO(user);
            userDTOS.add(userDTO);
        }
        userDTOS.sort(Comparator.comparingInt(UserDTO::getFollowers).reversed());
        return userDTOS;
    }

    @Override
    public boolean updateUser(EditUserRequest editUserRequest) throws Exception {
        if (!StringUtils.hasText(editUserRequest.getPassword())){
            logger.error("The password is required");
            throw new Exception("The password is required");
        }
        String userId = authorizePathService.getUserIdAuthoried();
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        if (!passwordEncoder.matches(editUserRequest.getPassword(), user.getPassword())) {
            logger.error("Password is incorrect");
            throw new Exception("Password is incorrect");
        } else {
            if (StringUtils.hasText(editUserRequest.getEmail())) {
                Users user_email = userRepository.findFirstByEmail(editUserRequest.getEmail());
                if (user_email != null){
                    logger.info("Email is registed!");
                } else user.setEmail(editUserRequest.getEmail());
            }
            if (StringUtils.hasText(editUserRequest.getNewPassword())) {
                user.setPassword(passwordEncoder.encode(editUserRequest.getNewPassword()));
            }
            if (editUserRequest.getAvatar() != null && !editUserRequest.getAvatar().isEmpty()){
                if(StringUtils.hasText(user.getAvtUrl())){
                    fileUploadsService.deleteFile(user.getAvtUrl());
                }
                String fileName = fileUploadsService.storeFile(editUserRequest.getAvatar(), "avatar", user.getUserId());
                user.setAvtUrl("avatar/"+fileName);
            }
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public boolean reportUser(String userId) throws Exception {
        String myId = authorizePathService.getUserIdAuthoried();
        if (myId.equals(userId)){
            logger.error("Can't report yourself");
            throw new Exception("Can't report yourself");
        }
        Users user = userRepository.findFirstByUserId(userId);
        if (user.getIsAdmin() == 1){
            logger.error("Can't report admin");
            throw new Exception("Can't report admin");
        }
        BlacklistUser blacklistUser = blacklistUserRepository.findByUserId(userId);
        if (blacklistUser == null){
            blacklistUser = new BlacklistUser();
            blacklistUser.setUserId(userId);
            blacklistUser.setReportedQuantity(1);
            blacklistUser.setBlockedAt(null);
            blacklistUserRepository.save(blacklistUser);
        } else {
            blacklistUser.setReportedQuantity(blacklistUser.getReportedQuantity() + 1);
            if (blacklistUser.getReportedQuantity() >= 1000){
                blacklistUser.setStatus("blocked");
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                Date nowSql = Timestamp.valueOf(now);
                blacklistUser.setBlockedAt(nowSql);
            }
            blacklistUserRepository.save(blacklistUser);
        }
        return true;
    }

    private UserDTO convertToUserDTO(Users user){
        UserDTO userDTO = new UserDTO();
        List<String> postId = new ArrayList<>();
        userDTO.setUserId(user.getUserId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAvtUrl(user.getAvtUrl());
        userDTO.setDob(user.getDob().toString());
        userDTO.setFollowers(user.getFolloweds().size());
        userDTO.setFollowings(user.getFollowers().size());
        userDTO.setPosts(user.getUserPosts().size());
        for (UserPosts userPosts : user.getUserPosts()){
            postId.add(userPosts.getUserPostId().getPostId());
        }
        userDTO.setPostId(postId);
        userDTO.setParticipantGroups(user.getParticipants().size());
        return userDTO;
    }
}
