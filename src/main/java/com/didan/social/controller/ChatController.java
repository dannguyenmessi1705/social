package com.didan.social.controller;

import com.didan.social.dto.CommentDTO;
import com.didan.social.dto.ConversationDTO;
import com.didan.social.dto.MessageDTO;
import com.didan.social.payload.ResponseData;
import com.didan.social.payload.request.SendMessageRequest;
import com.didan.social.service.impl.ChatServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Chat")
@RequestMapping("/chat")
public class ChatController {
    private final ChatServiceImpl chatService;
    @Autowired
    public ChatController(ChatServiceImpl chatService){
        this.chatService = chatService;
    }
    // Create Conversation
    @Operation(summary = "Create conversation to invite everyone to box chat",
                description = "Create conversation to invite everyone to box chat",
                security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/create")
    public ResponseEntity<?> createConversation(@RequestParam String conversationName){
        ResponseData payload = new ResponseData();
        Map<String, String> data = new HashMap<>();
        try {
            String conversationId = chatService.createConversation(conversationName);
            if (StringUtils.hasText(conversationId)){
                payload.setDescription("Create a conversation successful");
                data.put("conversationId: ", conversationId);
                data.put("conversationName:", conversationName);
                payload.setData(data);
            } else {
                payload.setDescription("Failed to create a conversation");
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
    // Join Conversation
    @Operation(summary = "Join conversation to chat",
            description = "Enter the id conversation to join",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/join/{conversation_id}")
    public ResponseEntity<?> joinConversation(@PathVariable(name = "conversation_id") String conversationId){
        ResponseData payload = new ResponseData();
        try {
            ConversationDTO data = chatService.joinConversation(conversationId);
            if (data != null){
                payload.setDescription(String.format("Join conversation %s successful", conversationId));
                payload.setData(data);
            } else {
                payload.setDescription("Cannot join this conversation");
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
    // Leave Conversation
    @Operation(summary = "Leave the conversation",
            description = "Enter id conversation you want to leave. When you leave the conversation, you cannot chat in it",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/leave/{conversation_id}")
    public ResponseEntity<?> leaveConversation(@PathVariable(name = "conversation_id") String conversationId){
        ResponseData payload = new ResponseData();
        try {
            boolean data = chatService.leaveConversation(conversationId);
            if (data){
                payload.setDescription(String.format("Leave conversation %s successful", conversationId));
            } else {
                payload.setDescription("Cannot leave this conversation");
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
    // Send Message
    @Operation(summary = "Send a message",
            description = "You can send a message in conversations you joined",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/send/{conversation_id}")
    public ResponseEntity<?> sendMessage(@PathVariable("conversation_id") String conversationId, @ModelAttribute SendMessageRequest sendMessageRequest){
        ResponseData payload = new ResponseData();
        try {
            MessageDTO data = chatService.sendMessage(conversationId, sendMessageRequest);
            if (data != null){
                payload.setDescription(String.format("Send message in conversation %s successful", conversationId));
                payload.setData(data);
            } else {
                payload.setDescription("Send message failed");
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
    // Get All Conversation User joined
    @GetMapping("/conversation/alls")
    @Operation(summary = "Get all conversations you joined",
            description = "Get all conversations you joined",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAllConversations(){
        ResponseData payload = new ResponseData();
        try {
            List<ConversationDTO> data = chatService.getAllConversation();
            if (data != null){
                payload.setDescription("Load all conversations you joined successful");
                payload.setData(data);
            } else {
                payload.setDescription("Failed to load conversations");
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
    // Search Conversation to Join
    @Operation(summary = "Search conversations you want to find",
            description = "Enter the name conversation you want to find",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/conversation")
    public ResponseEntity<?> searchConversation(@RequestParam(name = "name") String conversationName){
        ResponseData payload = new ResponseData();
        try {
            List<ConversationDTO> data = chatService.searchConversation(conversationName);
            if (data != null){
                payload.setDescription("Found conversations successful");
                payload.setData(data);
            } else {
                payload.setDescription("There is no conversation");
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
    // Get All Chat in Conversation User Join
    @Operation(summary = "Get all messages in a conversation you joined",
            description = "Entr the id conversation you want to get all messages",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/conversation/{conversation_id}")
    public ResponseEntity<?> getAllMessages(@PathVariable("conversation_id") String conversationId){
        ResponseData payload = new ResponseData();
        try {
            List<MessageDTO> data = chatService.getAllMessagesInConversation(conversationId);
            if (data != null){
                payload.setDescription("Load all messages successful");
                payload.setData(data);
            } else {
                payload.setDescription("There is no message in conversation");
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
