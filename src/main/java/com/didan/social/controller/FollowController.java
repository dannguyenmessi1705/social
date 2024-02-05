package com.didan.social.controller;

import com.didan.social.dto.FollowDTO;
import com.didan.social.payload.ResponseData;
import com.didan.social.service.impl.FollowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/follow")
public class FollowController {
    private final FollowServiceImpl followService;

    @Autowired
    public FollowController(FollowServiceImpl followService){
        this.followService = followService;
    }
    // Get Follower
    @GetMapping("/followers/{userId}")
    public ResponseEntity<?> getFollowers(@PathVariable String userId){
        ResponseData payload = new ResponseData();
        try {
            FollowDTO data = followService.getFollowers(userId);
            payload.setData(data);
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // Get Followings
    @GetMapping("/followings/{userId}")
    public ResponseEntity<?> getFollowings(@PathVariable String userId){
        ResponseData payload = new ResponseData();
        try {
            FollowDTO data = followService.getFollowings(userId);
            payload.setData(data);
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // Follow
    @PostMapping("/{userId}")
    public ResponseEntity<?> postFollow(@PathVariable String userId){
        ResponseData payload = new ResponseData();
        Map<String, String> data = new HashMap<>();
        try {
            if (followService.followUser(userId)){
                payload.setDescription("Follow this user successful");
                data.put("userId", userId);
                payload.setData(data);
            } else {
                payload.setDescription("Fail to follow this user");
                payload.setStatusCode(422);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // Unfollow
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> unfollow(@PathVariable String userId){
        ResponseData payload = new ResponseData();
        Map<String, String> data = new HashMap<>();
        try {
            if (followService.unfollowUser(userId)){
                payload.setDescription("Unfollow this user successful");
                data.put("userId", userId);
                payload.setData(data);
            } else {
                payload.setDescription("Fail to unfollow this user");
                payload.setStatusCode(422);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
