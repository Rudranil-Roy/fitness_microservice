package com.fitness.userservice.controller;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String user_id){
        return ResponseEntity.ok(userService.getUserProfile(user_id));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/{user_id}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String user_id){
        return ResponseEntity.ok(userService.existByUserId(user_id));
    }
}
