package com.didan.social.service;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.Users;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public List<UserDTO> getAllUser(){
        List<Users> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (Users user : users){
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setAvtUrl(user.getAvtUrl());
            userDTO.setDob(user.getDob());
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }
}
