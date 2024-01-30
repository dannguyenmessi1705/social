package com.didan.social.controller;

import com.didan.social.dto.UserDTO;
import com.didan.social.entity.Users;
import com.didan.social.payload.ResponseData;
import com.didan.social.payload.request.SignupRequest;
import com.didan.social.service.impl.AuthServiceImpl;
import com.didan.social.service.impl.FileUploadsServiceImpl;
import com.didan.social.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    public AuthController(AuthServiceImpl authService, JwtUtils jwtUtils, FileUploadsServiceImpl fileUploadsService){
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.fileUploadsService = fileUploadsService;
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
            payload.setStatusCode(400);
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
            Users user = authService.signup(signupRequest, signupRequest.getAvatar());
            if (user != null){
                payload.setDescription("SignUp Successful");
                response.put("userId", user.getUserId());
                response.put("accessToken", user.getAccessToken());
                payload.setData(response);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setDescription(e.getMessage());
            payload.setStatusCode(400);
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
            payload.setStatusCode(400);
            payload.setSuccess(false);
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/token-reset")
    public ResponseEntity<?> getTokenReset(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
