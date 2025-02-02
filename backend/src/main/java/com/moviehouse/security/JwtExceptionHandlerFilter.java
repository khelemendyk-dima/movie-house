package com.moviehouse.security;

import com.moviehouse.dto.ErrorDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.*;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            ErrorDto.handleException(response, HttpServletResponse.SC_UNAUTHORIZED, TOKEN_EXPIRED);
        } catch (JwtException e) {
            ErrorDto.handleException(response, HttpServletResponse.SC_UNAUTHORIZED, INVALID_TOKEN);
        } catch (Exception e) {
            ErrorDto.handleException(response, HttpServletResponse.SC_UNAUTHORIZED, AUTHENTICATION_ERROR);
        }
    }
}