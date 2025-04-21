package com.moviehouse.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        String secretKey = "746F702D7365637265742D6B65792D666F722D6A77742D746573742D313233";
        long expiration = 1000 * 60 * 60;

        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", expiration);

        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken(userDetails);
        boolean isValid = jwtUtil.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_shouldReturnFalseIfUsernameDoesNotMatch() {
        String token = jwtUtil.generateToken(userDetails);

        UserDetails anotherUser = mock(UserDetails.class);
        when(anotherUser.getUsername()).thenReturn("anotheruser");

        assertFalse(jwtUtil.isTokenValid(token, anotherUser));
    }

    @Test
    void isTokenExpired_shouldReturnFalseForFreshToken() {
        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractClaim(token, Claims::getExpiration);

        assertTrue(expiration.after(new Date()));
    }

    @Test
    void extractClaim_shouldReturnCustomClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");

        String token = jwtUtil.generateToken(claims, userDetails);

        String subject = jwtUtil.extractClaim(token, Claims::getSubject);
        assertEquals("testuser", subject);

        Claims allClaims = jwtUtil.extractClaim(token, c -> c);
        assertEquals("ADMIN", allClaims.get("role"));
    }
}
