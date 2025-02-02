package com.moviehouse.security;

import com.moviehouse.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.ACCESS_DENIED;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.warn("Access denied: {}", accessDeniedException.getMessage());

        ErrorDto.handleException(
                response,
                HttpServletResponse.SC_FORBIDDEN,
                ACCESS_DENIED
        );
    }
}