package com.saas.support.user.controller;

import com.saas.support.common.response.ApiResponse;
import com.saas.support.user.dto.UserResponse;
import com.saas.support.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User Management APIs")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(
                ApiResponse.success(userService.findAll()
                        .stream()
                        .map(u -> {
                            UserResponse r = new UserResponse();
                            r.setId(u.getId());
                            r.setEmail(u.getEmail());
                            r.setFirstName(u.getFirstName());
                            r.setLastName(u.getLastName());
                            r.setActive(u.isActive());
                            r.setCreatedAt(u.getCreatedAt());
                            return r;
                        }).toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        var u = userService.findById(id);
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setEmail(u.getEmail());
        r.setFirstName(u.getFirstName());
        r.setLastName(u.getLastName());
        r.setActive(u.isActive());
        r.setCreatedAt(u.getCreatedAt());
        return ResponseEntity.ok(ApiResponse.success(r));
    }
}