package com.moviehouse.service;

import com.moviehouse.model.Booking;

public interface EmailService {
    void sendBookingConfirmation(String email, Booking booking);
}
