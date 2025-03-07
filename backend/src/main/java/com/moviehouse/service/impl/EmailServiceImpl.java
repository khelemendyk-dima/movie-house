package com.moviehouse.service.impl;

import com.moviehouse.model.Booking;
import com.moviehouse.model.MovieSession;
import com.moviehouse.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${app.base.url}")
    private String baseUrl;

    private final JavaMailSender mailSender;

    @Override
    public void sendBookingConfirmation(String email, Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setTo(email);
            helper.setSubject("🎟 Your Movie Tickets");

            MovieSession session = booking.getSession();
            String movieName = session.getMovie().getTitle();
            String movieDate = session.getStartTime().toLocalDate().toString();
            String movieTime = session.getStartTime().toLocalTime().toString();
            String hallName = session.getHall().getName();
            String ticketUrl = baseUrl + "/api/bookings/" + booking.getId() + "/tickets/download";

            String emailContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
                    "<div style='max-width: 600px; margin: auto; padding: 20px; border-radius: 10px; border: 1px solid #ddd;'>" +
                    "    <img src='https://drive.usercontent.google.com/download?id=16GZKXYVEL749J7rZ0G7s-EwmjXFVyya8&export=download&authuser=0' alt='MovieHouse Logo' style='max-width: 200px;'/>" +
                    "    <h2 style='color: #333;'>🎟 Your Movie Tickets Are Ready! 🎬</h2>" +
                    "    <p style='font-size: 16px; color: #666;'>Thank you for your purchase! Your movie tickets are now available.</p>" +
                    "    <p style='font-size: 16px; color: #666;'>Movie: <strong>" + movieName + "</strong></p>" +
                    "    <p style='font-size: 16px; color: #666;'>Date and time: <strong>" + movieDate + " " + movieTime + "</strong></p>" +
                    "    <p style='font-size: 16px; color: #666;'>Hall: <strong>" + hallName + "</strong></p>" +
                    "    <a href='" + ticketUrl + "' style='display: inline-block; padding: 12px 20px; color: white; background-color: #ff5733; text-decoration: none; font-size: 16px; border-radius: 5px;'>Download Tickets</a>" +
                    "    <p style='margin-top: 20px; font-size: 14px; color: #777;'>Need help? Contact us at <a href='mailto:support@moviehouse.com'>support@moviehouse.com</a></p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }

}
