package com.moviehouse.exception.handler;

import com.moviehouse.exception.AlreadyExistsException;
import com.moviehouse.exception.NotFoundException;
import com.moviehouse.exception.PaymentRequiredException;
import com.moviehouse.exception.ServiceException;
import com.moviehouse.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.INVALID_LOGIN_DATA;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleSpringValidationException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorDto> handleServiceException(ServiceException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(BadCredentialsException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(createErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                INVALID_LOGIN_DATA
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PaymentRequiredException.class)
    public ResponseEntity<ErrorDto> handlePaymentRequiredException(PaymentRequiredException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(createErrorResponse(
                HttpStatus.PAYMENT_REQUIRED.value(),
                e.getMessage()
        ), HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(createErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleAlreadyExistsException(AlreadyExistsException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(createErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception e) {
        log.error(e.getMessage());

        return new ResponseEntity<>(createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                SERVER_ERROR + e.getMessage()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ErrorDto createErrorResponse(Integer status, String message) {
        return ErrorDto.builder()
                .status(status)
                .timestamp(System.currentTimeMillis())
                .message(message)
                .build();
    }
}