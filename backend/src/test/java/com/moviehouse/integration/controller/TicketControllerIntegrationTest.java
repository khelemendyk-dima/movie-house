package com.moviehouse.integration.controller;

import com.moviehouse.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TicketControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void validateTicket_shouldReturn200_whenValidTicket() {
        String response = get("/api/tickets/validate/-1")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        assertEquals("Ticket is valid!", response);
    }

    @Test
    void validateTicket_shouldReturn402_whenBookingNotPaid() {
        get("/api/tickets/validate/-2")
                .then()
                .statusCode(402)
                .body("status", equalTo(402),
                        "timestamp", notNullValue(),
                        "message", equalTo("Booking with id='-2' not paid successfully"));
    }

    @Test
    void validateTicket_shouldReturn404_whenTicketNotFound() {
        get("/api/tickets/validate/987654321")
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("Ticket with id='987654321' not found"));
    }

    @Test
    void validateTicket_shouldReturn409_whenValidTicket() {
        get("/api/tickets/validate/-1")
                .then()
                .statusCode(200);

        get("/api/tickets/validate/-1")
                .then()
                .statusCode(409)
                .body("status", equalTo(409),
                        "timestamp", notNullValue(),
                        "message", equalTo("Ticket with id='-1' already used"));
    }
}
