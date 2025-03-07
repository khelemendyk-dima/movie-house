package com.moviehouse.scheduler;

import com.moviehouse.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCleanupTask {
    private final BookingService bookingService;

    @Scheduled(fixedRateString = "${booking.cleanup.fixed-rate}")
    public void deleteExpiredBookings() {
        bookingService.removeExpiredBookings();
    }
}
