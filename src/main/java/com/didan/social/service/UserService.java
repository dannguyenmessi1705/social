package com.didan.social.service;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.UserPosts;
import com.didan.social.entity.Users;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class UserService implements UserServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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
            userDTO.setDob(user.getDob());
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
            userDTO.setDob(user.getDob());
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
            userDTO.setDob(user.getDob());
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
}
