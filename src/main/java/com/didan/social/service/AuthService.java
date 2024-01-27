package com.didan.social.service;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.Users;
import com.didan.social.payload.request.SignupRequest;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.AuthServiceImpl;
import com.didan.social.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService implements AuthServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Autowired
    public AuthService (UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
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
            userDTO.setResetExp(user.getResetExp());
            userDTO.setResetToken(user.getResetToken());
            userDTO.setRefreshToken(user.getRefreshToken());
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }
    @Override
    public UserDTO login(String email, String password) throws Exception{
        Users user = userRepository.findFirstByEmail(email);
        if(user == null) throw new Exception("Email is not existed");
        UserDTO userDTO = new UserDTO();
        if (passwordEncoder.matches(password, user.getPassword())){
            userDTO.setUserId(user.getUserId());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setAvtUrl(user.getAvtUrl());
            userDTO.setDob(user.getDob());
            userDTO.setResetExp(user.getResetExp());
            userDTO.setResetToken(user.getResetToken());
            userDTO.setRefreshToken(user.getRefreshToken());
            return userDTO;
        }
        else throw new Exception("Email and Password does not match");
    }
    @Override
    public UserDTO signup(SignupRequest signupRequest) throws Exception{
        UserDTO userDTO = new UserDTO();
        Users user = userRepository.findFirstByEmail(signupRequest.getEmail());
        if(user != null) throw new Exception("Email is existed");
        if(signupRequest.getPassword().length() < 5) throw new Exception("The minimum password should be 5");
        else{
            UUID id = UUID.randomUUID();
            userDTO.setUserId(id.toString());
            Users userSave = new Users();
            userSave.setUserId(id.toString());
            userSave.setEmail(signupRequest.getEmail());
            userSave.setFullName(signupRequest.getFullName());
            userSave.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            userSave.setAvtUrl(signupRequest.getAvtUrl());
            String refreshToken = jwtUtils.generateRefreshToken(signupRequest.getEmail());
            userSave.setRefreshToken(refreshToken);
            userDTO.setRefreshToken(refreshToken);
            String dateString = signupRequest.getDob();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            userSave.setDob(sqlDate);
            userRepository.save(userSave);
            return userDTO;
        }
    }
}
