package com.moviehouse.integration.controller;

import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.BookingDto;
import com.moviehouse.dto.MovieSessionDto;
import com.moviehouse.integration.BaseIntegrationTest;
import com.moviehouse.model.BookingStatus;
import com.moviehouse.model.Seat;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.BOOKING_NOT_PAID;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_EMAIL;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookingControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void createBooking_shouldReturn200_whenValidRequest() {
        BookingDto bookingDto = TestDataFactory.createBookingDto(BookingStatus.PENDING);
        MovieSessionDto movieSessionDto = getTestMovieSession();
        bookingDto.setSessionId(movieSessionDto.getId());
        bookingDto.setSeatIds(getSeatIdsByHallId(movieSessionDto.getHallId(), 3));

        post("/api/bookings", bookingDto)
                .then()
                .statusCode(200)
                .body("bookingId", notNullValue(),
                        "sessionId", notNullValue(),
                        "name", equalTo("John Doe"),
                        "email", equalTo("john.doe@example.com"),
                        "phone", equalTo("+380991112233"),
                        "seatIds", hasSize(3),
                        "totalPrice", equalTo("30.00"),
                        "status", equalTo("PENDING"));
    }


    @Test
    void createBooking_shouldReturn400_whenInvalidRequest() {
        BookingDto bookingDto = TestDataFactory.createBookingDto(BookingStatus.PENDING);
        bookingDto.setEmail(null);

        post("/api/bookings", bookingDto)
                .then()
                .statusCode(400)
                .body("status", equalTo(400),
                        "timestamp", notNullValue(),
                        "message", equalTo(EMPTY_EMAIL));
    }

    @Test
    void createBooking_shouldReturn404_whenSessionNotFound() {
        BookingDto bookingDto = TestDataFactory.createBookingDto(BookingStatus.PENDING);
        bookingDto.setSessionId(987654321L);

        post("/api/bookings", bookingDto)
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("Movie session with id='987654321' not found"));
    }

    @Test
    void downloadTickets_shouldReturn402_whenBookingNotPaid() {
        BookingDto bookingDto = TestDataFactory.createBookingDto(BookingStatus.PENDING);
        MovieSessionDto movieSessionDto = getTestMovieSession();
        bookingDto.setSessionId(movieSessionDto.getId());
        bookingDto.setSeatIds(getSeatIdsByHallId(movieSessionDto.getHallId(), 3));

        int bookingId = post("/api/bookings", bookingDto)
                .then()
                .extract()
                .jsonPath()
                .getInt("bookingId");

        get("/api/bookings/" + bookingId + "/tickets/download")
                .then()
                .statusCode(402)
                .body("status", equalTo(402),
                        "timestamp", notNullValue(),
                        "message", equalTo(format(BOOKING_NOT_PAID, bookingId)));
    }

    private MovieSessionDto getTestMovieSession() {
        int movieId = get("/api/movies")
                .then()
                .extract().jsonPath().getInt("[0].id");

        return get("/api/sessions?movieId=" + movieId)
                .then()
                .extract()
                .jsonPath()
                .getObject("[0]", MovieSessionDto.class);
    }

    private List<Long> getSeatIdsByHallId(Long hallId, int numberOfSeats) {
        return get("/api/halls/" + hallId)
                .then()
                .extract()
                .jsonPath()
                .getList("seats", Seat.class)
                .stream()
                .limit(numberOfSeats)
                .map(Seat::getId)
                .toList();
    }
}
