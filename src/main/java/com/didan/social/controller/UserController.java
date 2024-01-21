package com.didan.social.controller;

import com.didan.social.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    @Qualifier("UserService")
    LoginService loginService;
    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(){
        return new ResponseEntity<>(loginService.getAllUser(), HttpStatus.OK);
    }
}
