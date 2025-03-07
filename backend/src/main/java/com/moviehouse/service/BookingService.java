package com.moviehouse.service;

import com.moviehouse.dto.BookingDto;


public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto);

    boolean isBookingPaid(Long bookingId);
}
