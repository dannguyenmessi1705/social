package com.didan.social.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    // Get Comments in Post
    @GetMapping("/post/{post_id}")
    public ResponseEntity<?> getCommentInPost(@PathVariable("post_id") String post_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Post Comment
    @PostMapping("/post/{post_id}")
    public ResponseEntity<?> postCommentInPost(@PathVariable("post_id") String post_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Get Comment By Id
    @GetMapping("/{comment_id}")
    public ResponseEntity<?> getCommentById(@PathVariable("comment_id") String comment_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // React Comment
    @PostMapping("/{comment_id}")
    public ResponseEntity<?> reactComment(@PathVariable("comment_id") String comment_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Delete React Comment
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> delReactComment(@PathVariable("comment_id") String comment_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Update Comment
    @PatchMapping("/update/{comment_id}")
    public ResponseEntity<?> updateComment(@PathVariable("comment_id") String comment_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Delete Comment
    @DeleteMapping("/delete/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") String comment_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
