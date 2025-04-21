package com.moviehouse.service.impl;

import com.moviehouse.exception.ServiceException;
import com.moviehouse.model.*;
import com.moviehouse.config.TestDataFactory;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private Booking booking;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(emailService, "baseUrl", "http://localhost:8080");

        Movie movie = TestDataFactory.createMovie();
        Hall hall = TestDataFactory.createHall();
        MovieSession session = TestDataFactory.createMovieSession(movie, hall);

        booking = TestDataFactory.createBooking(BookingStatus.PAID, session);
    }

    @Test
    void sendBookingConfirmation_shouldSendEmailSuccessfully() {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendBookingConfirmation("test@example.com", booking);

        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendBookingConfirmation_shouldThrowException_whenMessagingFails() {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Simulated failure"))
                .when(mailSender).send(mimeMessage);

        assertThrows(ServiceException.class, () ->
                emailService.sendBookingConfirmation("fail@example.com", booking));

        verify(mailSender).send(mimeMessage);
    }
}
