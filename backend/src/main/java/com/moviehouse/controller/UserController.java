package com.moviehouse.controller;

import com.moviehouse.dto.UserDto;
import com.moviehouse.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of all users.")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Received request to get all users");

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        log.info("Received request to get user by id={}", id);

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data."),
            @ApiResponse(responseCode = "404", description = "User not found."),
    })
    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,
                                              @RequestBody @Valid UserDto userDto) {
        log.info("Received request to update user with id={}", id);

        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("id") Long id) {
        log.info("Received request to delete user with id={}", id);

        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
