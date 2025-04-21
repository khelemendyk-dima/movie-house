package com.moviehouse.config;

import com.moviehouse.dto.*;
import com.moviehouse.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestDataFactory {

    public static AuthDto createAuthDto() {
        return AuthDto.builder()
                .token("mock-token")
                .user(createUserDto("USER"))
                .build();
    }

    public static BookingDto createBookingDto(BookingStatus status) {
        BookingDto dto = new BookingDto();
        dto.setBookingId(1L);
        dto.setSessionId(1L);
        dto.setName("John Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPhone("+380991112233");
        dto.setSeatIds(List.of(1L, 2L, 3L));
        dto.setTotalPrice("20.00");
        dto.setStatus(status);

        return dto;
    }

    public static BookingDto createBookingDto(BookingStatus status, List<Seat> seats) {
        BookingDto dto = new BookingDto();
        dto.setBookingId(1L);
        dto.setSessionId(1L);
        dto.setName("John Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPhone("+380991112233");
        dto.setSeatIds(seats.stream().map(Seat::getId).toList());
        dto.setTotalPrice("20.00");
        dto.setStatus(status);

        return dto;
    }

    public static HallDto createHallDto() {
        HallDto dto = new HallDto();
        dto.setId(1L);
        dto.setName("Main Hall");
        dto.setRowCount(1);
        dto.setSeatsPerRow(3);
        dto.setSeats(createSeatDtos());

        return dto;
    }

    public static LoginDto createLoginDto() {
        LoginDto dto = new LoginDto();
        dto.setEmail("john.doe@example.com");
        dto.setPassword("SecurePass123!");

        return dto;
    }

    public static MovieDto createMovieDto() {
        MovieDto dto = new MovieDto();
        dto.setId(1L);
        dto.setTitle("Epic Movie");
        dto.setDescription("An epic tale of adventure.");
        dto.setDuration(120);
        dto.setAgeRating("PG-13");
        dto.setReleaseDate(LocalDate.now());
        dto.setPosterUrl("https://example.com/poster.jpg");
        dto.setGenres(Set.of("Action", "Adventure"));

        return dto;
    }

    public static MovieSessionDto createMovieSessionDto() {
        MovieSessionDto dto = new MovieSessionDto();
        dto.setId(1L);
        dto.setMovieId(1L);
        dto.setHallId(1L);
        dto.setStartTime(LocalDateTime.now().plusDays(1));
        dto.setPrice(BigDecimal.valueOf(10.50));

        return dto;
    }

    public static MovieSessionRegistrationDto createMovieSessionRegistrationDto() {
        MovieSessionRegistrationDto dto = new MovieSessionRegistrationDto();
        dto.setMovieId(1L);
        dto.setHallId(1L);
        dto.setStartTime(LocalTime.of(18, 0));
        dto.setStartDate(LocalDate.now().plusDays(1));
        dto.setEndDate(LocalDate.now().plusDays(10));
        dto.setPrice(BigDecimal.valueOf(12.00));

        return dto;
    }

    public static PaymentRequest createPaymentRequest() {
        PaymentRequest dto = new PaymentRequest();
        dto.setBookingId(1L);
        dto.setSuccessUrl("https://example.com/success");
        dto.setCancelUrl("https://example.com/cancel");

        return dto;
    }

    public static RegistrationDto createRegistrationDto() {
        RegistrationDto dto = new RegistrationDto();
        dto.setName("John Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPassword("SecurePass123!");
        dto.setConfirmPassword("SecurePass123!");

        return dto;
    }

    public static List<SeatDto> createSeatDtos() {
        List<SeatDto> dtos = new ArrayList<>();

        dtos.add(createSeatDto(1L, 1, 1));
        dtos.add(createSeatDto(2L, 1, 2));
        dtos.add(createSeatDto(3L, 1, 3));

        return dtos;
    }

    public static SeatDto createSeatDto(Long seatId, Integer rowNumber, Integer seatNumber) {
        SeatDto dto = new SeatDto();
        dto.setId(seatId);
        dto.setRowNumber(rowNumber);
        dto.setSeatNumber(seatNumber);

        return dto;
    }

    public static List<SeatStatusDto> createSeatStatusDtos() {
        List<SeatStatusDto> dtos = new ArrayList<>();
        dtos.add(new SeatStatusDto(1L, 1, 1, "FREE"));
        dtos.add(new SeatStatusDto(2L, 1, 2, "RESERVED"));

        return dtos;
    }

    public static TicketDto createTicketDto() {
        TicketDto dto = new TicketDto();
        dto.setUsername("John Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPhone("+380991112233");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setRowNumber(1);
        dto.setSeatNumber(5);
        dto.setUsed(false);

        return dto;
    }

    public static UserDto createUserDto(String roleName) {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("John Doe");
        dto.setEmail("john.doe@example.com");
        dto.setRole(roleName);

        return dto;
    }

    public static Booking createBooking(BookingStatus status, MovieSession session) {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setSession(session);
        booking.setName("John Doe");
        booking.setEmail("john.doe@example.com");
        booking.setPhone("+380991112233");
        booking.setStatus(status);
        booking.setTotalPrice(BigDecimal.valueOf(20.00));
        booking.setCreatedAt(LocalDateTime.now());

        return booking;
    }

    public static Genre createGenre(Long id, String name) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);

        return genre;
    }

    public static Hall createHall() {
        Hall hall = new Hall();
        hall.setId(1L);
        hall.setName("Main Hall");
        hall.setRowCount(1);
        hall.setSeatsPerRow(3);
        hall.setSeats(createSeats(hall));

        return hall;
    }

    public static Movie createMovie() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Epic Movie");
        movie.setDescription("An epic tale of adventure.");
        movie.setDuration(120);
        movie.setAgeRating("PG-13");
        movie.setReleaseDate(LocalDate.now());
        movie.setPosterUrl("https://example.com/poster.jpg");

        Set<Genre> genres = new HashSet<>();
        genres.add(createGenre(1L, "Action"));
        genres.add(createGenre(2L, "Adventure"));

        movie.setGenres(genres);

        return movie;
    }

    public static MovieSession createMovieSession() {
        MovieSession session = new MovieSession();
        session.setId(1L);
        session.setHall(createHall());
        session.setStartTime(LocalDateTime.now().plusDays(1));
        session.setPrice(BigDecimal.valueOf(10.50));

        return session;
    }

    public static MovieSession createMovieSession(Movie movie, Hall hall) {
        MovieSession session = createMovieSession();
        session.setMovie(movie);
        session.setHall(hall);

        return session;
    }

    public static Role createRole(String name) {
        Role role = new Role();
        role.setId(1L);
        role.setName(name);

        return role;
    }

    public static Seat createSeat(Hall hall) {
        Seat seat = new Seat();
        seat.setId(1L);
        seat.setHall(hall);
        seat.setRowNumber(1);
        seat.setSeatNumber(5);

        return seat;
    }

    public static Seat createSeat(Long id, Hall hall, Integer rowNumber, Integer seatNumber) {
        Seat seat = new Seat();
        seat.setId(id);
        seat.setHall(hall);
        seat.setRowNumber(rowNumber);
        seat.setSeatNumber(seatNumber);

        return seat;
    }

    public static List<Seat> createSeats(Hall hall) {
        List<Seat> seats = new ArrayList<>();

        seats.add(createSeat(1L, hall, 1, 1));
        seats.add(createSeat(2L, hall, 1, 2));
        seats.add(createSeat(3L, hall, 1, 3));

        return seats;
    }

    public static Ticket createTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setUsed(false);

        Seat seat = new Seat();
        seat.setSeatNumber(5);
        seat.setRowNumber(2);
        ticket.setSeat(seat);

        return ticket;
    }

    public static Ticket createTicket(Booking booking, MovieSession session, Seat seat) {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setBooking(booking);
        ticket.setSession(session);
        ticket.setSeat(seat);
        ticket.setUsed(false);

        return ticket;
    }

    public static User createUser(Role role) {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("SecurePass123!");
        user.setRole(role);

        return user;
    }
}
