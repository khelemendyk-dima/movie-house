package com.moviehouse.integration.controller;

import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.HallDto;
import com.moviehouse.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.ACCESS_DENIED;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class HallControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void getAllHalls_shouldReturn200_whenValidRequest() {
        get("/api/halls")
                .then()
                .statusCode(200)
                .body("$", hasSize(2),
                        "[0].name", equalTo("Big Hall 1"),
                        "[1].name", equalTo("VIP Hall 2"));
    }

    @Test
    void getHallById_shouldReturn200_whenHallExists() {
        HallDto hallDto = TestDataFactory.createHallDto();

        Integer hallId = createTestHall(hallDto);

        get("/api/halls/" + hallId)
                .then()
                .statusCode(200)
                .body("id", equalTo(hallId),
                        "name", equalTo(hallDto.getName()),
                        "rowCount", equalTo(hallDto.getRowCount()),
                        "seatsPerRow", equalTo(hallDto.getSeatsPerRow()),
                        "seats", hasSize(hallDto.getSeats().size()));
    }

    @Test
    void getHallById_shouldReturn404_whenHallNotFound() {
        get("/api/halls/987654321")
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("Hall with id='987654321' not found"));
    }

    @Test
    void createHall_shouldReturn200_whenValidRequest() {
        HallDto hallDto = TestDataFactory.createHallDto();

        givenLoggedInAdmin()
                .when()
                .body(hallDto)
                .post("/api/halls")
                .then()
                .statusCode(200)
                .body("id", notNullValue(),
                        "name", equalTo(hallDto.getName()),
                        "rowCount", equalTo(hallDto.getRowCount()),
                        "seatsPerRow", equalTo(hallDto.getSeatsPerRow()),
                        "seats", hasSize(hallDto.getSeats().size()));
    }

    @Test
    void createHall_shouldReturn400_whenInvalidRequest() {
        HallDto hallDto = TestDataFactory.createHallDto();
        hallDto.setName(null);

        givenLoggedInAdmin()
                .when()
                .body(hallDto)
                .post("/api/halls")
                .then()
                .statusCode(400)
                .body("status", equalTo(400),
                        "timestamp", notNullValue(),
                        "message", equalTo(EMPTY_NAME));
    }

    @Test
    void createHall_shouldReturn401_whenUnauthorized() {
        HallDto hallDto = TestDataFactory.createHallDto();

        post("/api/halls", hallDto)
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void createHall_shouldReturn403_whenUserRole() {
        HallDto hallDto = TestDataFactory.createHallDto();

        givenLoggedInUser()
                .when()
                .body(hallDto)
                .post("/api/halls")
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void updateHall_shouldReturn200_whenHallUpdated() {
        HallDto hallDto = TestDataFactory.createHallDto();

        Integer hallId = createTestHall(hallDto);

        hallDto.setName("UPDATED HALL");

        givenLoggedInAdmin()
                .when()
                .body(hallDto)
                .put("/api/halls/" + hallId)
                .then()
                .statusCode(200)
                .body("id", equalTo(hallId),
                        "name", equalTo("UPDATED HALL"));
    }

    @Test
    void updateHall_shouldReturn400_whenInvalidRequest() {
        HallDto hallDto = TestDataFactory.createHallDto();

        Integer hallId = createTestHall(hallDto);

        hallDto.setName(null);

        givenLoggedInAdmin()
                .when()
                .body(hallDto)
                .put("/api/halls/" + hallId)
                .then()
                .statusCode(400)
                .body("status", equalTo(400),
                        "timestamp", notNullValue(),
                        "message", equalTo(EMPTY_NAME));
    }

    @Test
    void updateHall_shouldReturn401_whenInvalidRequest() {
        HallDto hallDto = TestDataFactory.createHallDto();

        put("/api/halls/987654321", hallDto)
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void updateHall_shouldReturn403_whenUserRole() {
        HallDto hallDto = TestDataFactory.createHallDto();

        givenLoggedInUser()
                .when()
                .body(hallDto)
                .put("/api/halls/987654321")
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void updateHall_shouldReturn404_whenHallNotFound() {
        HallDto hallDto = TestDataFactory.createHallDto();

        givenLoggedInAdmin()
                .when()
                .body(hallDto)
                .put("/api/halls/987654321")
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("Hall with id='987654321' not found"));
    }

    @Test
    void deleteHall_shouldReturn200_whenHallDeleted() {
        HallDto hallDto = TestDataFactory.createHallDto();

        Integer hallId = createTestHall(hallDto);

        givenLoggedInAdmin()
                .when()
                .delete("/api/halls/" + hallId)
                .then()
                .statusCode(200)
                .body("id", equalTo(hallId),
                        "name", equalTo(hallDto.getName()));

        get("/api/halls/" + hallId)
                .then()
                .statusCode(404);
    }

    @Test
    void deleteHall_shouldReturn401_whenHallDeleted() {
        delete("/api/halls/987654321")
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void deleteHall_shouldReturn403_whenUserRole() {
        givenLoggedInUser()
                .when().delete("/api/halls/987654321")
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void deleteHall_shouldReturn404_whenHallNotFound() {
        givenLoggedInAdmin()
                .when().delete("/api/halls/987654321")
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("Hall with id='987654321' not found"));
    }

    private Integer createTestHall(HallDto hallDto) {
        return givenLoggedInAdmin()
                .when()
                .body(hallDto)
                .post("/api/halls")
                .then()
                .statusCode(200)
                .extract().jsonPath().get("id");
    }
}
