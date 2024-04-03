package com.didan.social.controller;

import com.didan.social.dto.CommentDTO;
import com.didan.social.dto.PostDTO;
import com.didan.social.payload.ResponseData;
import com.didan.social.payload.request.CreateCommentRequest;
import com.didan.social.payload.request.EditCommentRequest;
import com.didan.social.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Comment")
@RequestMapping("/comment")
public class CommentController {
    private final CommentServiceImpl commentService;
    @Autowired
    public CommentController(CommentServiceImpl commentService){
        this.commentService = commentService;
    }
    /* 
    Get Comments in Post
    */  
    
    @GetMapping("/post/{post_id}")
    @Operation(summary = "Get all comments in a post",
            description = "Enter the id post to get all comments",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getCommentInPost(@PathVariable("post_id") String postId){
        ResponseData payload = new ResponseData();
        try {
            List<CommentDTO> data = commentService.getCommentsInPost(postId);
            if (data == null){
                payload.setDescription("No comments in here");
            } else {
                payload.setDescription(String.format("Load all comments in post %s successful", postId));
                payload.setData(data);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // Post Comment
    @Operation(summary = "Post comment in a post",
            description = "Require id post and properties of the comment",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/post/{post_id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> postCommentInPost(@PathVariable("post_id") String postId, @ModelAttribute CreateCommentRequest createCommentRequest){
        ResponseData payload = new ResponseData();
        Map<String, String> data = new HashMap<>();
        try {
            String commentId = commentService.postCommentInPost(postId, createCommentRequest);
            if (StringUtils.hasText(commentId)){
                payload.setDescription(String.format("Up comment in post %s successful", postId));
                data.put("commentId: ", commentId);
                payload.setData(data);
            } else {
                payload.setDescription("Up comment failed");
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
    // Get Comment By Id
    @Operation(summary = "Get detail a comment",
            description = "Enter the id comment to get detail",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{comment_id}")
    public ResponseEntity<?> getCommentById(@PathVariable("comment_id") String commentId){
        ResponseData payload = new ResponseData();
        try {
            CommentDTO data = commentService.getCommentById(commentId);
            if (data == null){
                payload.setDescription("No comment is here");
            } else {
                payload.setDescription(String.format("Load comment %s successful", commentId));
                payload.setData(data);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // React Comment
    @Operation(summary = "Like comment",
            description = "Enter the id comment you want to like",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{comment_id}")
    public ResponseEntity<?> reactComment(@PathVariable("comment_id") String commentId){
        ResponseData payload = new ResponseData();
        try {
            if (commentService.likeComment(commentId)){
                payload.setDescription("Like comment successful");
            } else {
                payload.setDescription("No comment in here");
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // Delete React Comment
    @Operation(summary = "Unlike comment",
            description = "Enter the id comment you want to unlike",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> delReactComment(@PathVariable("comment_id") String commentId){
        ResponseData payload = new ResponseData();
        try {
            if (commentService.unlikeComment(commentId)){
                payload.setDescription("Unlike comment successful");
            } else {
                payload.setDescription("No comment in here");
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // Update Comment
    @Operation(summary = "Edit/Update the comment",
            description = "Enter id comment then properties of comment you want to edit/update",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping(value = "/update/{comment_id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateComment(@PathVariable("comment_id") String commentId, @ModelAttribute EditCommentRequest editCommentRequest){
        ResponseData payload = new ResponseData();
        try {
            CommentDTO data = commentService.updateComment(commentId, editCommentRequest);
            if (data != null){
                payload.setDescription("Update comment successful");
                payload.setData(data);
            } else {
                payload.setDescription("Update comment failed");
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
    // Delete Comment
    @Operation(summary = "Delete the comment",
            description = "Enter the id your comment you want to delete",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") String commentId){
        ResponseData payload = new ResponseData();
        try {
            if (commentService.deleteComment(commentId)){
                payload.setDescription("Delete comment successful");
            } else {
                payload.setDescription("No comment in here");
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
