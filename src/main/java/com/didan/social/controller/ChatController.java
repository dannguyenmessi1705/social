package com.didan.social.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    // Create Conversation
    @PostMapping("/create")
    public ResponseEntity<?> createConversation(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Join Conversation
    @PostMapping("/join")
    public ResponseEntity<?> joinConversation(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Leave Conversation
    @PostMapping("/leave")
    public ResponseEntity<?> leaveConversation(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Send Message
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Get All Conversation
    @GetMapping("/conversation/alls")
    public ResponseEntity<?> getAllConversations(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    // Get All Chat in Conversation
    @GetMapping("/{conversation_id}")
    public ResponseEntity<?> getAllMessages(@PathVariable("conversation_id") String conversation_id){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
