package com.moviehouse.controller;

import com.moviehouse.dto.AuthDto;
import com.moviehouse.dto.LoginDto;
import com.moviehouse.dto.RegistrationDto;
import com.moviehouse.dto.UserDto;
import com.moviehouse.service.AuthService;
import com.moviehouse.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {
    @Value("${security.jwt.cookie-max-age}")
    private int cookieMaxAge;

    private final AuthService authenticationService;
    private final UserService userService;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data."),
            @ApiResponse(responseCode = "409", description = "Email is already in use.")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegistrationDto registrationRequest) {
        log.info("Received request to register new user with email={}", registrationRequest.getEmail());

        return ResponseEntity.ok(authenticationService.register(registrationRequest));
    }

    @Operation(summary = "Authenticate a user and generate a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data."),
            @ApiResponse(responseCode = "401", description = "Invalid credentials.")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthDto> authenticate(@RequestBody @Valid LoginDto request, HttpServletResponse response) {
        log.info("Received request to authenticate user with email={}", request.getEmail());

        AuthDto authDto = authenticationService.authenticate(request);

        Cookie jwtCookie = createCookie("jwt", authDto.getToken(), cookieMaxAge);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok(authDto);
    }

    @Operation(summary = "Logout user by clearing the JWT cookie")
    @ApiResponse(responseCode = "200", description = "Logout successful.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        log.info("Received request to logout user by clearing the JWT cookie");

        Cookie resetJwtCookie = createCookie("jwt", null, 0);

        response.addCookie(resetJwtCookie);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "User is not authenticated.")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Received request to get current user with email={}", userDetails.getUsername());

        UserDto userDTO = userService.getUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok(userDTO);
    }

    private Cookie createCookie(String name, String value, int cookieMaxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(cookieMaxAge);

        return cookie;
    }
}
