package com.didan.social.controller;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.Users;
import com.didan.social.payload.ResponseData;
import com.didan.social.payload.request.SignupRequest;
import com.didan.social.service.impl.AuthServiceImpl;
import com.didan.social.service.impl.FileUploadsServiceImpl;
import com.didan.social.service.impl.MailServiceImpl;
import com.didan.social.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl authService;
    private final JwtUtils jwtUtils;
    private final FileUploadsServiceImpl fileUploadsService;
    private final MailServiceImpl mailService;
    @Autowired
    public AuthController(AuthServiceImpl authService, JwtUtils jwtUtils, FileUploadsServiceImpl fileUploadsService, MailServiceImpl mailService){
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.fileUploadsService = fileUploadsService;
        this.mailService = mailService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> postLogin(@RequestParam String email, @RequestParam String password){
        ResponseData payload = new ResponseData();
        Map<String, String> response = new HashMap<>();
        try{
            Users user = authService.login(email, password);
            if (user != null) {
                payload.setDescription("Login Successful");
                response.put("userId", user.getUserId());
                response.put("accessToken", user.getAccessToken());
                payload.setData(response);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setDescription(e.getMessage());
            payload.setStatusCode(500);
            payload.setSuccess(false);
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    // Dùng @ModelAtrribute để upload file + Json trên form-data
    @PostMapping(value = "/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> postSignup(@ModelAttribute SignupRequest signupRequest){
        ResponseData payload = new ResponseData();
        Map<String, String> response = new HashMap<>();
        try {
            Users user = authService.signup(signupRequest);
            if (user != null){
                payload.setDescription("SignUp Successful");
                response.put("userId", user.getUserId());
                response.put("accessToken", user.getAccessToken());
                payload.setData(response);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setDescription(e.getMessage());
            payload.setStatusCode(500);
            payload.setSuccess(false);
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        ResponseData payload = new ResponseData();
        try{
            authService.logout();
            payload.setDescription("Logout successful");
            return new ResponseEntity<>(payload, HttpStatus.OK);
        }catch (Exception e){
            payload.setDescription(e.getMessage());
            payload.setStatusCode(500);
            payload.setSuccess(false);
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/token-reset")
    public ResponseEntity<?> getTokenReset(@RequestParam(value = "email") String email){
        ResponseData payload = new ResponseData();
        Map<String, String> data = new HashMap<>();
        try {
            String token = authService.requestForgot(email);
            if(StringUtils.hasText(token)){
                payload.setDescription("Email sent, please check inbox");
                data.put("token", token);
                payload.setData(data);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setStatusCode(500);
            payload.setSuccess(false);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PatchMapping("/reset")
    public ResponseEntity<?> updatePassword(@RequestParam String token, @RequestParam String newPassword){
        ResponseData payload = new ResponseData();
        try{
            if(authService.updatePassword(token, newPassword)){
                payload.setDescription("Reset password successful");
                return new ResponseEntity<>(payload, HttpStatus.OK);
            } else {
                payload.setDescription("Reset password false");
                payload.setStatusCode(422);
                return new ResponseEntity<>(payload, HttpStatus.OK);
            }
        } catch (Exception e){
            payload.setStatusCode(500);
            payload.setSuccess(false);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
