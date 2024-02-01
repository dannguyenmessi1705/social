package com.didan.social.service;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.BlacklistToken;
import com.didan.social.entity.Users;
import com.didan.social.payload.request.SignupRequest;
import com.didan.social.repository.BlacklistRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.AuthServiceImpl;
import com.didan.social.service.impl.FileUploadsServiceImpl;
import com.didan.social.service.impl.MailServiceImpl;
import com.didan.social.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.sendgrid.*;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AuthService implements AuthServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final BlacklistRepository blacklistRepository;
    private final FileUploadsServiceImpl fileUploadsService;
    private final Environment env;
    private final MailServiceImpl mailService;
    private final HttpServletRequest request;
    @Autowired
    public AuthService (UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtils jwtUtils,
                        BlacklistRepository blacklistRepository,
                        FileUploadsServiceImpl fileUploadsService,
                        Environment env,
                        MailServiceImpl mailService,
                        HttpServletRequest request
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.blacklistRepository = blacklistRepository;
        this.fileUploadsService = fileUploadsService;
        this.env = env;
        this.mailService = mailService;
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
            mailService.sendTextEmail(signupRequest.getEmail(), "WELCOME", "<h1>You signup successfully. Welcome to our service</h1>");
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

    @Override
    public String requestForgot(String email) throws Exception{
        Users user = userRepository.findFirstByEmail(email);
        if(user == null) throw new Exception("Email is not existed");
        UUID token = UUID.randomUUID();
        user.setResetToken(token.toString());

        // Thiết lập hạn token
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDateTime expTime = now.plus(10, ChronoUnit.MINUTES);
        Date resetExp = Timestamp.valueOf(expTime);
        user.setResetExp(resetExp);

        userRepository.save(user);

        // send email
        String protocol = request.getScheme();
        String host = request.getHeader("host");
        String http = protocol + "://" + host;
        String html = "<h1>You requested reset passsword. Now click this <a href=" + "\"" + http +"/auth/reset/" + token.toString() + "\">link</a> to reset password";
        mailService.sendTextEmail(email, "RESET PASSWORD", html);
        return token.toString();
    }

    // Verify Token Reset
    @Override
    public String verifyToken(String token) throws Exception{
        Users user = userRepository.findFirstByResetToken(token);
        if (user == null) throw new Exception("Request error");
        else {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            Date nowDate = Timestamp.valueOf(now);
            if (nowDate.after(user.getResetExp())){
                return null;
            } else {
                return token;
            }
        }
    }

    @Override
    public boolean updatePassword(String token, String newPassword) throws Exception {
        if (StringUtils.hasText(verifyToken(token))){
            if (newPassword.length() < 5) return false;
            else {
                Users user = userRepository.findFirstByResetToken(token);
                String passEncode = passwordEncoder.encode(newPassword);
                user.setPassword(passEncode);
                userRepository.save(user);
                return true;
            }
        }
        else return false;
    }
}
