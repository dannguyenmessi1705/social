package com.didan.social.controller;

import com.didan.social.dto.UserDTO;
import com.didan.social.payload.ResponseData;
import com.didan.social.payload.request.SignupRequest;
import com.didan.social.service.impl.AuthServiceImpl;
import com.didan.social.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl authService;
    private final JwtUtils jwtUtils;
    @Autowired
    public AuthController(AuthServiceImpl authService, JwtUtils jwtUtils){
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }
    @PostMapping("/signin")
    public ResponseEntity<?> postLogin(@RequestParam String email, @RequestParam String password){
        ResponseData payload = new ResponseData();
        Map<String, String> response = new HashMap<>();
        try{
            UserDTO user = authService.login(email, password);
            if (user != null) {
                payload.setDescription("Login Successful");
                response.put("userId", user.getUserId());
                response.put("accessToken", jwtUtils.generateAccessToken(user.getEmail()));
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

    @PostMapping("/signup")
    public ResponseEntity<?> postSignup(@RequestBody SignupRequest signupRequest){
        ResponseData payload = new ResponseData();
        Map<String, String> response = new HashMap<>();
        try {
            UserDTO user = authService.signup(signupRequest);
            if (user != null){
                payload.setDescription("SignUp Successful");
                response.put("userId", user.getUserId());
                response.put("accessToken", jwtUtils.generateAccessToken(user.getEmail()));
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

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
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
