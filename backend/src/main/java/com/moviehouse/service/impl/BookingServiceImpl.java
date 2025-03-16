package com.moviehouse.service.impl;

import com.moviehouse.dto.BookingDto;
import com.moviehouse.exceptions.*;
import com.moviehouse.model.*;
import com.moviehouse.repository.BookingRepository;
import com.moviehouse.repository.MovieSessionRepository;
import com.moviehouse.repository.SeatRepository;
import com.moviehouse.service.BookingService;
import com.moviehouse.util.ConvertorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Value("${booking.cleanup.expiration-minutes}")
    private int expirationMinutes;

    private final BookingRepository bookingRepository;
    private final MovieSessionRepository movieSessionRepository;
    private final SeatRepository seatRepository;
    private final ConvertorUtil convertor;

    @Transactional
    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        MovieSession session = findMovieSessionById(bookingDto.getSessionId());
        List<Seat> seats = validateSeats(bookingDto.getSeatIds(), session);

        Booking booking = createBookingEntity(bookingDto, session, seats);
        bookingRepository.save(booking);

        return convertor.toBookingDto(booking);
    }

    @Override
    public boolean isBookingPaid(Long bookingId) {
        return bookingRepository.existsByIdAndStatus(bookingId, BookingStatus.PAID);
    }

    @Transactional
    @Override
    public void removeExpiredBookings() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(expirationMinutes);
        int deletedCount = bookingRepository.deleteExpiredBookings(tenMinutesAgo, BookingStatus.PENDING);

        if (deletedCount > 0) {
            log.info("Deleted {} expired unpaid bookings.", deletedCount);
        }
    }

    private MovieSession findMovieSessionById(Long id) {
        return movieSessionRepository.findById(id)
                .orElseThrow(() -> new MovieSessionNotFoundException(id));
    }

    private List<Seat> validateSeats(List<Long> seatIds, MovieSession session) {
        List<Seat> seats = seatRepository.findAllById(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new SeatNotFoundException(seatIds);
        }

        boolean invalidSeats = seats.stream().anyMatch(seat -> !seat.getHall().getId().equals(session.getHall().getId()));
        if (invalidSeats) {
            throw new InvalidSeatForHallException(session.getId(), session.getHall().getId(), seatIds);
        }

        if (seatRepository.areSeatsTaken(session.getId(), seatIds)) {
            throw new SeatAlreadyBookedException(seatIds);
        }

        return seats;
    }

    private Booking createBookingEntity(BookingDto bookingDto, MovieSession session, List<Seat> seats) {
        Booking booking = new Booking();
        booking.setName(bookingDto.getName());
        booking.setEmail(bookingDto.getEmail());
        booking.setPhone(bookingDto.getPhone());
        booking.setSession(session);
        booking.setStatus(BookingStatus.PENDING);

        List<Ticket> tickets = seats.stream()
                .map(seat -> createTicketEntity(session, seat, booking))
                .toList();

        booking.setTickets(tickets);
        booking.setTotalPrice(calculateTotalPrice(tickets, session));

        return booking;
    }

    private static Ticket createTicketEntity(MovieSession session, Seat seat, Booking booking) {
        Ticket ticket = new Ticket();
        ticket.setSession(session);
        ticket.setBooking(booking);
        ticket.setSeat(seat);

        return ticket;
    }

    private BigDecimal calculateTotalPrice(List<Ticket> tickets, MovieSession session) {
        return BigDecimal.valueOf(tickets.size()).multiply(session.getPrice());
    }
}
