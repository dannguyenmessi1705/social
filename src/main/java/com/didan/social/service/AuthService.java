package com.didan.social.service;


import com.didan.social.entity.BlacklistToken;
import com.didan.social.entity.Users;
import com.didan.social.payload.request.SignupRequest;
import com.didan.social.repository.BlacklistRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.AuthServiceImpl;
import com.didan.social.service.impl.AuthorizePathServiceImpl;
import com.didan.social.service.impl.FileUploadsServiceImpl;
import com.didan.social.service.impl.MailServiceImpl;
import com.didan.social.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class AuthService implements AuthServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistRepository blacklistRepository;
    private final FileUploadsServiceImpl fileUploadsService;
    private final MailServiceImpl mailService;
    private final AuthorizePathServiceImpl authorizePathService;
    private final JwtUtils jwtUtils;
    @Autowired
    public AuthService (UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        BlacklistRepository blacklistRepository,
                        FileUploadsServiceImpl fileUploadsService,
                        JwtUtils jwtUtils,
                        MailServiceImpl mailService,
                        AuthorizePathServiceImpl authorizePathService
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizePathService = authorizePathService;
        this.blacklistRepository = blacklistRepository;
        this.fileUploadsService = fileUploadsService;
        this.mailService = mailService;
        this.jwtUtils = jwtUtils;
    }
    @Override
    public Users login(String email, String password) throws Exception{
        Users user = userRepository.findFirstByEmail(email);
        if(user == null) {
            logger.error("Email is not existed");
            throw new Exception("Email is not existed");
        }
        if (passwordEncoder.matches(password, user.getPassword())){
            if (StringUtils.hasText(user.getAccessToken())){
                BlacklistToken blacklistToken = new BlacklistToken(user.getAccessToken());
                blacklistRepository.save(blacklistToken);
            }
            user.setAccessToken(jwtUtils.generateAccessToken(user.getUserId()));
            userRepository.save(user);
            return user;
        }
        else {
            logger.error("Email and Password does not match");
            throw new Exception("Email and Password does not match");
        }
    }
    @Override
    public Users signup(SignupRequest signupRequest) throws Exception{
        Users user = userRepository.findFirstByEmail(signupRequest.getEmail());
        if(user != null) {
            logger.error("Email is existed");
            throw new Exception("Email is existed");
        }
        if(signupRequest.getPassword().length() < 5) {
            logger.error("The minimum password should be 5");
            throw new Exception("The minimum password should be 5");
        }
        else{
            UUID id = UUID.randomUUID();
            Users userSave = new Users();
            userSave.setIsAdmin(0);
            String fileName = fileUploadsService.storeFile(signupRequest.getAvatar(), "avatar", id.toString());
            userSave.setAvtUrl("avatar/"+fileName);
            userSave.setUserId(id.toString());
            userSave.setEmail(signupRequest.getEmail());
            userSave.setFullName(signupRequest.getFullName());
            userSave.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            String dateString = signupRequest.getBirthday();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            userSave.setDob(sqlDate);
            userSave.setAccessToken(jwtUtils.generateAccessToken(id.toString()));
            userRepository.save(userSave);
            mailService.sendTextEmail(signupRequest.getEmail(), "WELCOME", "<h1>You signup successfully. Welcome to our service</h1>");
            return userSave;
        }
    }

    @Override
    public void logout() throws Exception {
        String userId = authorizePathService.getUserIdAuthoried();
        Users user = userRepository.findFirstByUserId(userId);
        if(user == null) {
            logger.error("User is not existed");
            throw new Exception("User is not existed");
        }
        if(StringUtils.hasText(user.getAccessToken())){
            BlacklistToken blacklistToken = new BlacklistToken(user.getAccessToken());
            blacklistRepository.save(blacklistToken);
        } else{
            logger.error("Server error");
            throw new Exception("Server error");
        }
    }

    @Override
    public String requestForgot(String email) throws Exception{
        Users user = userRepository.findFirstByEmail(email);
        if(user == null) throw new Exception("Email is not existed");
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(6);
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        String token = sb.toString();
        user.setResetToken(token);

        // Thiết lập hạn token
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDateTime expTime = now.plusMinutes(10);
        Date resetExp = Timestamp.valueOf(expTime);
        user.setResetExp(resetExp);

        userRepository.save(user);

        // send email
        String html = "<h1>You requested reset passsword. The OTP is <b>" + token + "</b>";
        mailService.sendTextEmail(email, "RESET PASSWORD", html);
        return token;
    }

    // Verify Token Reset
    @Override
    public String verifyToken(String token) throws Exception{
        Users user = userRepository.findFirstByResetToken(token);
        if (user == null) {
            logger.error("Request error");
            throw new Exception("Request error");
        }
        else {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            Date nowDate = Timestamp.valueOf(now);
            if (nowDate.after(user.getResetExp())){
                logger.error("Expired token");
                return null;
            } else {
                return token;
            }
        }
    }

    @Override
    public boolean updatePassword(String token, String newPassword) throws Exception {
        if (StringUtils.hasText(verifyToken(token))){
            if (newPassword.length() < 5) {
                logger.error("The minimum password should be 5");
                return false;
            }
            else {
                Users user = userRepository.findFirstByResetToken(token);
                String passEncode = passwordEncoder.encode(newPassword);
                user.setPassword(passEncode);
                userRepository.save(user);
                logger.info("Update password successful");
                return true;
            }
        }
        else {
            logger.error("Cannot update password");
            return false;
        }
    }
}
