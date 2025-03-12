package com.moviehouse.controller;

import com.moviehouse.dto.AuthDto;
import com.moviehouse.dto.LoginDto;
import com.moviehouse.dto.RegistrationDto;
import com.moviehouse.dto.UserDto;
import com.moviehouse.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${security.jwt.cookie-max-age}")
    private int cookieMaxAge;

    private final AuthService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegistrationDto registrationRequest) {
        return ResponseEntity.ok(authenticationService.register(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> authenticate(@RequestBody @Valid LoginDto request, HttpServletResponse response) {
        AuthDto authDto = authenticationService.authenticate(request);

        Cookie jwtCookie = new Cookie("jwt", authDto.getToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(cookieMaxAge);

        response.addCookie(jwtCookie);

        return ResponseEntity.ok(authDto.getUser());
    }
}
