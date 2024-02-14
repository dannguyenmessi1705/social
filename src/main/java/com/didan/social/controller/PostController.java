package com.didan.social.controller;

import com.didan.social.dto.PostDTO;
import com.didan.social.payload.ResponseData;
import com.didan.social.payload.request.CreatePostRequest;
import com.didan.social.payload.request.EditPostRequest;
import com.didan.social.service.PostService;
import com.didan.social.service.impl.PostServiceImpl;
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
@Tag(name = "Post")
@RequestMapping("/post")
public class PostController {
    private final PostServiceImpl postService;
    @Autowired
    public PostController(PostServiceImpl postService){
        this.postService = postService;
    }
    // Get Post
    @Operation(summary = "Get posts by page",
            description = "Enter the page number you want to get posts",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/get")
    public ResponseEntity<?> getPosts(@RequestParam(value = "page", required = false) Integer page){
        if (page == null){
            page = 1;
        }
        ResponseData payload = new ResponseData();
        try {
            List<PostDTO> postDTOs = postService.getAllPosts(page);
            if (postDTOs.size() <= 0){
                payload.setDescription("No posts in here");
            } else {
                payload.setDescription("Load all posts successful");
                payload.setData(postDTOs);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // Create one Post
    @Operation(summary = "Create a post",
            description = "Create a post by entering properties of post",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createPost(@ModelAttribute CreatePostRequest createPostRequest){
        ResponseData payload = new ResponseData();
        Map<String, String> data = new HashMap<>();
        try {
            String postId = postService.createPost(createPostRequest);
            if (StringUtils.hasText(postId)){
                payload.setDescription("Create post successful");
                data.put("postId: ", postId);
                payload.setData(data);
            } else {
                payload.setDescription("Create post failed");
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
    // Get Detail One Post
    @Operation(summary = "Get detail a post",
            description = "Enter the id post you want to get detail",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPostById(@PathVariable("post_id") String postId) {
        ResponseData payload = new ResponseData();
        try {
            PostDTO postDTO = postService.getPostById(postId);
            if (postDTO == null){
                payload.setDescription("No post in here");
            } else {
                payload.setDescription("Load post successful");
                payload.setData(postDTO);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    // Search Post
    @Operation(summary = "Search post",
            description = "Enter the name post you want to find",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search")
    public ResponseEntity<?> searchPostById(@RequestParam(name = "name") String name) {
        ResponseData payload = new ResponseData();
        try {
            List<PostDTO> postDTOs = postService.getPostByTitle(name);
            if (postDTOs.size() <= 0){
                payload.setDescription("No post in here");
            } else {
                payload.setDescription("Load post successful");
                payload.setData(postDTOs);
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    // React Post
    @Operation(summary = "Like post",
            description = "Enter the id post you want to like",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{post_id}")
    public ResponseEntity<?> reactPost(@PathVariable("post_id") String postId) {
        ResponseData payload = new ResponseData();
        try {
            if (postService.likePost(postId)){
                payload.setDescription("Like post successful");
            } else {
                payload.setDescription("No post in here");
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // Delete React Post
    @Operation(summary = "Unlike post",
            description = "Enter the id post you want to unlike",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("{post_id}")
    public ResponseEntity<?> delReactPost(@PathVariable("post_id") String postId){
        ResponseData payload = new ResponseData();
        try {
            if (postService.unlikePost(postId)){
                payload.setDescription("Unlike post successful");
            } else {
                payload.setDescription("No post in here");
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setSuccess(false);
            payload.setStatusCode(500);
            payload.setDescription(e.getMessage());
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    // Update Post
    @Operation(summary = "Update/Edit post",
            description = "Enter the id post you want to update/edit",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping(value = "/update/{post_id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updatePost(@PathVariable("post_id") String postId, @ModelAttribute EditPostRequest editPostRequest){
        ResponseData payload = new ResponseData();
        try {
            PostDTO postDTO = postService.updatePost(postId, editPostRequest);
            if (postDTO != null){
                payload.setDescription("Update post successful");
                payload.setData(postDTO);
            } else {
                payload.setDescription("Update post failed");
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
    // Delete Post
    @Operation(summary = "Delte post",
            description = "Enter the id post you want to delete",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<?> deletePost(@PathVariable("post_id") String postId){
        ResponseData payload = new ResponseData();
        try {
            if (postService.deletePost(postId)){
                payload.setDescription("Delete post successful");
            } else {
                payload.setDescription("No post in here");
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
