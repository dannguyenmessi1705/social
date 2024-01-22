package com.didan.social.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    // Get Post
    @GetMapping("/get")
    public ResponseEntity<?> getPosts(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Create one Post
    @PostMapping("/new")
    public ResponseEntity<?> createPost(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Get Detail One Post
    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPostById(@PathVariable("post_id") String post_id) {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // React Post
    @PostMapping("/{post_id}")
    public ResponseEntity<?> reactPost(@PathVariable("post_id") String post_id) {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Delete React Post
    @DeleteMapping("{post_id}")
    public ResponseEntity<?> delReactPost(@PathVariable("post_id") String post_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Update Post
    @PutMapping("/update/{post_id}")
    public ResponseEntity<?> updatePost(@PathVariable("post_id") String post_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Delete Post
    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<?> deletePost(@PathVariable("post_id") String post_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
