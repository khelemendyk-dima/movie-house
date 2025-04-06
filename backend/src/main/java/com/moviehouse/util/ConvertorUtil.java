package com.moviehouse.util;

import com.moviehouse.dto.*;
import com.moviehouse.exception.RoleNotFoundException;
import com.moviehouse.model.*;
import com.moviehouse.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.ROLE_BY_NAME_NOT_FOUND;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class ConvertorUtil {
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto toUserDto(User user) {
        UserDto userDTO = modelMapper.map(user, UserDto.class);
        userDTO.setRole(user.getRole().getName());

        return userDTO;
    }

    public User toUser(RegistrationDto request) {
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException(format(ROLE_BY_NAME_NOT_FOUND, "USER")));

        User user = modelMapper.map(request, User.class);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return user;
    }

    public MovieDto toMovieDto(Movie movie) {
        MovieDto movieDto = modelMapper.map(movie, MovieDto.class);

        Set<String> genres = movie.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());

        movieDto.setGenres(genres);

        return movieDto;
    }

    public Movie toMovie(MovieDto movieDto) {
        return modelMapper.map(movieDto, Movie.class);
    }

    public MovieSessionDto toMovieSessionDto(MovieSession movieSession) {
        return modelMapper.map(movieSession, MovieSessionDto.class);
    }

    public HallDto toHallDto(Hall hall) {
        HallDto hallDto = modelMapper.map(hall, HallDto.class);

        hallDto.setSeats(hall.getSeats().stream()
                .map(seat -> modelMapper.map(seat, SeatDto.class))
                .toList());

        return hallDto;
    }

    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);

        bookingDto.setSessionId(booking.getSession().getId());

        bookingDto.setSeatIds(booking.getTickets().stream()
                .map(ticket -> ticket.getSeat().getId())
                .toList());

        return bookingDto;
    }
}
