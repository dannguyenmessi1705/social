package com.didan.social.service;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.BlacklistToken;
import com.didan.social.entity.Users;
import com.didan.social.payload.request.SignupRequest;
import com.didan.social.repository.BlacklistRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.AuthServiceImpl;
import com.didan.social.service.impl.FileUploadsServiceImpl;
import com.didan.social.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
    private final BlacklistRepository blacklistRepository;
    private final FileUploadsServiceImpl fileUploadsService;
    private final HttpServletRequest request;
    @Autowired
    public AuthService (UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, BlacklistRepository blacklistRepository, FileUploadsServiceImpl fileUploadsService, HttpServletRequest request){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.blacklistRepository = blacklistRepository;
        this.fileUploadsService = fileUploadsService;
        this.request = request;
    }
    @Override
    public Users login(String email, String password) throws Exception{
        Users user = userRepository.findFirstByEmail(email);
        if(user == null) throw new Exception("Email is not existed");
        if (passwordEncoder.matches(password, user.getPassword())){
            if (StringUtils.hasText(user.getAccessToken())){
                BlacklistToken blacklistToken = new BlacklistToken(user.getAccessToken());
                blacklistRepository.save(blacklistToken);
            }
            user.setAccessToken(jwtUtils.generateAccessToken(user.getEmail()));
            userRepository.save(user);
            return user;
        }
        else throw new Exception("Email and Password does not match");
    }
    @Override
    public Users signup(SignupRequest signupRequest, MultipartFile avatar) throws Exception{
        Users user = userRepository.findFirstByEmail(signupRequest.getEmail());
        if(user != null) throw new Exception("Email is existed");
        if(signupRequest.getPassword().length() < 5) throw new Exception("The minimum password should be 5");
        else{
            UUID id = UUID.randomUUID();
            Users userSave = new Users();
            String fileName = fileUploadsService.storeFile(avatar, "avatar", id.toString());
            userSave.setAvtUrl("avatar/"+fileName);
            userSave.setUserId(id.toString());
            userSave.setEmail(signupRequest.getEmail());
            userSave.setFullName(signupRequest.getFullName());
            userSave.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            String dateString = signupRequest.getDob();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            userSave.setDob(sqlDate);
            userSave.setAccessToken(jwtUtils.generateAccessToken(signupRequest.getEmail()));
            userRepository.save(userSave);
            return userSave;
        }
    }

    @Override
    public void logout() throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (accessToken == null) throw new Exception("Not Authorization");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        Users user = userRepository.findFirstByEmail(email);
        if(user == null) throw new Exception("User is not existed");
        if(StringUtils.hasText(user.getAccessToken())){
            BlacklistToken blacklistToken = new BlacklistToken(user.getAccessToken());
            blacklistRepository.save(blacklistToken);
        } else throw new Exception("Server error");
    }
}
