package com.didan.social.service;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.UserPosts;
import com.didan.social.entity.Users;
import com.didan.social.payload.request.EditUserRequest;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.FileUploadsServiceImpl;
import com.didan.social.service.impl.UserServiceImpl;
import com.didan.social.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class UserService implements UserServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadsService fileUploadsService;
    @Autowired
    public UserService(UserRepository userRepository,
                       JwtUtils jwtUtils,
                       HttpServletRequest request,
                       PasswordEncoder passwordEncoder,
                       FileUploadsService fileUploadsService
    ){
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.request = request;
        this.passwordEncoder = passwordEncoder;
        this.fileUploadsService = fileUploadsService;
    }
    @Override
    public List<UserDTO> getAllUser(){
        List<Users> users = userRepository.findAll();
        if (users.size() <= 0) {
            logger.info("No one");
            return null;
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        for (Users user : users){
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
            userDTOS.add(userDTO);
        }
        Collections.sort(userDTOS, Comparator.comparingInt(UserDTO::getFollowers).reversed());
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

    @Override
    public List<UserDTO> searchUser(String searchName) throws Exception {
        List<Users> users = userRepository.findByFullNameContainingOrEmailLike(searchName, searchName);
        if (users.size() <= 0) {
            logger.info("No one");
            return null;
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        for (Users user : users){
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
            userDTOS.add(userDTO);
        }
        Collections.sort(userDTOS, Comparator.comparingInt(UserDTO::getFollowers).reversed());
        return userDTOS;
    }

    @Override
    public boolean updateUser(EditUserRequest editUserRequest) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) {
            logger.error("Not Authorized");
            throw new Exception("Not Authorized");
        }
        if (!StringUtils.hasText(editUserRequest.getPassword())){
            logger.error("The password is required");
            throw new Exception("The password is required");
        }
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) {
            logger.error("Have some errors");
            throw new Exception("Have some errors");
        }
        Users user = userRepository.findFirstByEmail(email);
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
}
