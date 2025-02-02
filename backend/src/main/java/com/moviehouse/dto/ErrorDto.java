package com.moviehouse.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;

import java.io.IOException;

@Data
@Builder
public class ErrorDto {
    Integer status;
    Long timestamp;
    String message;

    public static ErrorDto createErrorResponse(Integer status, String message) {
        return ErrorDto.builder()
                .status(status)
                .timestamp(System.currentTimeMillis())
                .message(message)
                .build();
    }

    public static void handleException(HttpServletResponse response, Integer status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorDto errorResponse = ErrorDto.builder()
                .status(status)
                .timestamp(System.currentTimeMillis())
                .message(message)
                .build();

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}