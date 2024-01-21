package com.didan.social.service;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.Users;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("UserService")
public class LoginService implements LoginServiceImpl {
    @Autowired
    @Qualifier("UserRepository")
    UserRepository userRepository;
    @Override
    public List<UserDTO> getAllUser(){
        List<Users> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (Users user : users){
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setAvtUrl(user.getAvtUrl());
            userDTO.setDob(user.getDob());
            userDTO.setResetExp(user.getResetExp());
            userDTO.setResetToken(user.getResetToken());
            userDTO.setRefreshToken(user.getRefreshToken());
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }
    @Override
    public UserDTO login(String email, String password){
        Users users = userRepository.findFirstByEmailAndPassword(email, password);
        if(users == null) return null;
        UserDTO userDTO = new UserDTO(users.getUserId(), users.getFullName(), users.getEmail(), users.getPassword(), users.getAvtUrl(), users.getDob(), users.getResetExp(), users.getResetToken(), users.getRefreshToken());
        return userDTO;
    }
}
