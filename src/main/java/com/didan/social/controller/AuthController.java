package com.didan.social.controller;

import com.didan.social.dto.UserDTO;
import com.didan.social.payload.Payload;
import com.didan.social.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    LoginServiceImpl loginService;
    @PostMapping("/signin")
    public ResponseEntity<?> postLogin(@RequestParam String email, @RequestParam String password){
        Payload payload = new Payload();
        UserDTO users = loginService.login(email, password);
        if (users != null) {
            payload.setData(users);
        } else {
            payload.setStatusCode(401);
            payload.setData("email or password is failed");
        }
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> postSignup(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
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
