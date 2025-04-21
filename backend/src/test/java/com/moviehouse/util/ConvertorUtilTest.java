package com.moviehouse.util;

import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.*;
import com.moviehouse.exception.RoleNotFoundException;
import com.moviehouse.model.*;
import com.moviehouse.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConvertorUtilTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ConvertorUtil convertorUtil;

    @Test
    void toUserDto_shouldMapUserToDto() {
        Role role = TestDataFactory.createRole("USER");
        User user = TestDataFactory.createUser(role);

        UserDto userDto = TestDataFactory.createUserDto("USER");
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = convertorUtil.toUserDto(user);

        assertEquals(userDto, result);

        verify(modelMapper).map(user, UserDto.class);
    }

    @Test
    void toUser_shouldMapRegistrationDtoToUser() {
        RegistrationDto dto = TestDataFactory.createRegistrationDto();

        Role role = TestDataFactory.createRole("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPass");

        User mappedUser = TestDataFactory.createUser(role);
        when(modelMapper.map(dto, User.class)).thenReturn(mappedUser);

        User result = convertorUtil.toUser(dto);

        assertEquals("encodedPass", result.getPassword());
        assertEquals(role, result.getRole());

        verify(roleRepository).findByName("USER");
    }

    @Test
    void toUser_shouldThrowException_whenRoleNotFound() {
        RegistrationDto dto = TestDataFactory.createRegistrationDto();

        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> convertorUtil.toUser(dto));
    }

    @Test
    void toMovieDto_shouldMovieToDto() {
        Movie movie = TestDataFactory.createMovie();

        MovieDto movieDto = TestDataFactory.createMovieDto();
        when(modelMapper.map(movie, MovieDto.class)).thenReturn(movieDto);

        MovieDto result = convertorUtil.toMovieDto(movie);

        assertEquals(movieDto, result);
    }

    @Test
    void toMovie_shouldMapMovieDtoToMovie() {
        MovieDto movieDto = TestDataFactory.createMovieDto();
        Movie movie = TestDataFactory.createMovie();

        when(modelMapper.map(movieDto, Movie.class)).thenReturn(movie);

        assertEquals(movie, convertorUtil.toMovie(movieDto));
    }

    @Test
    void toMovieSessionDto_shouldMapMovieSession() {
        MovieSession session = TestDataFactory.createMovieSession();
        MovieSessionDto sessionDto = TestDataFactory.createMovieSessionDto();
        when(modelMapper.map(session, MovieSessionDto.class)).thenReturn(sessionDto);

        assertEquals(sessionDto, convertorUtil.toMovieSessionDto(session));
    }

    @Test
    void toHallDto_shouldMapSeats() {
        Hall hall = TestDataFactory.createHall();
        Seat seat = TestDataFactory.createSeat(1L, hall, 1, 2);
        hall.setSeats(List.of(seat));

        HallDto hallDto = new HallDto();
        when(modelMapper.map(hall, HallDto.class)).thenReturn(hallDto);
        when(modelMapper.map(seat, SeatDto.class)).thenReturn(new SeatDto());

        HallDto result = convertorUtil.toHallDto(hall);

        assertEquals(1, result.getSeats().size());
    }

    @Test
    void toBookingDto_shouldMapBookingToDto() {
        MovieSession session = TestDataFactory.createMovieSession();
        Booking booking = TestDataFactory.createBooking(BookingStatus.PENDING, session);
        
        Seat seat = new Seat();
        seat.setId(100L);
        Ticket ticket = new Ticket();
        ticket.setSeat(seat);
        booking.setTickets(List.of(ticket));

        BookingDto bookingDto = TestDataFactory.createBookingDto(BookingStatus.PENDING, List.of(seat));
        when(modelMapper.map(booking, BookingDto.class)).thenReturn(bookingDto);

        BookingDto result = convertorUtil.toBookingDto(booking);

        assertEquals(1L, result.getSessionId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("+380991112233", result.getPhone());
        assertEquals(BookingStatus.PENDING, result.getStatus());
        assertTrue(result.getSeatIds().contains(100L));
    }

    @Test
    void toTicketDto_shouldMapTicketDetails() {
        Booking booking = new Booking();
        booking.setName("Alice");
        booking.setPhone("111");
        booking.setEmail("alice@example.com");
        booking.setCreatedAt(LocalDateTime.now());

        Seat seat = new Seat();
        seat.setSeatNumber(3);
        seat.setRowNumber(2);

        Ticket ticket = new Ticket();
        ticket.setBooking(booking);
        ticket.setSeat(seat);
        ticket.setUsed(true);

        TicketDto result = convertorUtil.toTicketDto(ticket);

        assertEquals("Alice", result.getUsername());
        assertEquals("alice@example.com", result.getEmail());
        assertEquals(3, result.getSeatNumber());
        assertTrue(result.isUsed());
    }
}
