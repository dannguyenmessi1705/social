package com.didan.social.controller;

import com.didan.social.payload.ResponseData;
import com.didan.social.service.AuthService;
import com.didan.social.service.UserService;
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
    UserService userService;
    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(){
        ResponseData payload = new ResponseData();
        try{
            payload.setData(userService.getAllUser());
            payload.setDescription("Get all user successful");
            return new ResponseEntity<>(payload, HttpStatus.OK);
        }catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription("Server Error");
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
