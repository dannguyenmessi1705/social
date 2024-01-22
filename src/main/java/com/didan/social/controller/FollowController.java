package com.didan.social.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {
    // Get Follower
    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Get Followings
    @GetMapping("/followings")
    public ResponseEntity<?> getFollowings(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Follow
    @PostMapping("/new")
    public ResponseEntity<?> postFollow(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Unfollow
    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unfollow(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
