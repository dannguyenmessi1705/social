package com.didan.social.controller;

import com.didan.social.payload.ResponseData;
import com.didan.social.service.impl.AdminServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Admin")
@RequestMapping("/admin")
public class AdminController{
    private final AdminServiceImpl adminService;
    @Autowired
    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }
    // Grant admin
    @Operation(summary = "Grant user for admin role", description = "Require admin to do this",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping(value = "/grant")
    public ResponseEntity<?> grantUser(@RequestParam String userId){
        ResponseData payload = new ResponseData();
        try {
            if (adminService.grantAdmin(userId)){
                payload.setDescription("Grant user for admin role successful");
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setDescription(e.getMessage());
            payload.setStatusCode(500);
            payload.setSuccess(false);
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Operation(summary = "Get all users in blacklist", description = "Require admin to do this",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/blacklist")
    public ResponseEntity<?> getAllBlacklistUser(){
        ResponseData payload = new ResponseData();
        try {
            payload.setData(adminService.getAllBlacklistUser());
            payload.setDescription("Get all users in blacklist successful");
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setDescription(e.getMessage());
            payload.setStatusCode(500);
            payload.setSuccess(false);
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @Operation(summary = "Ban user", description = "Require admin to do this",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/ban/{userId}")
    public ResponseEntity<?> blockUser(@PathVariable String userId){
        ResponseData payload = new ResponseData();
        try {
            if (adminService.blockUser(userId)){
                payload.setDescription(String.format("Block user %s successful", userId));
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setDescription(e.getMessage());
            payload.setStatusCode(500);
            payload.setSuccess(false);
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @Operation(summary = "Unban user", description = "Require admin to do this",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/unban/{userId}")
    public ResponseEntity<?> unblockUser(@PathVariable String userId){
        ResponseData payload = new ResponseData();
        try {
            if (adminService.unblockUser(userId)){
                payload.setDescription(String.format("Unblock user %s successful", userId));
            }
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e){
            payload.setDescription(e.getMessage());
            payload.setStatusCode(500);
            payload.setSuccess(false);
            return new ResponseEntity<>(payload, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
