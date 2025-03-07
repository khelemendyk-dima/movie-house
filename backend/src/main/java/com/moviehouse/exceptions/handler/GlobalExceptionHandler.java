package com.moviehouse.exceptions.handler;

import com.moviehouse.exceptions.AlreadyExistsException;
import com.moviehouse.exceptions.NotFoundException;
import com.moviehouse.exceptions.PaymentRequiredException;
import com.moviehouse.exceptions.ServiceException;
import com.moviehouse.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.INVALID_LOGIN_DATA;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleSpringValidationException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorDto.createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorDto> handleServiceException(ServiceException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorDto.createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(BadCredentialsException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorDto.createErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                INVALID_LOGIN_DATA
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PaymentRequiredException.class)
    public ResponseEntity<ErrorDto> handlePaymentRequiredException(PaymentRequiredException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorDto.createErrorResponse(
                HttpStatus.PAYMENT_REQUIRED.value(),
                e.getMessage()
        ), HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorDto.createErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleAlreadyExistsException(AlreadyExistsException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorDto.createErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception e) {
        log.error(e.getMessage());

        return new ResponseEntity<>(ErrorDto.createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                SERVER_ERROR + e.getMessage()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}