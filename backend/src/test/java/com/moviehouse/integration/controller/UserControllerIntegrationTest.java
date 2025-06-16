package com.moviehouse.integration.controller;

import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.RegistrationDto;
import com.moviehouse.dto.UserDto;
import com.moviehouse.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.ACCESS_DENIED;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_EMAIL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void getAllUsers_shouldReturn200_whenValidRequest() {
        givenLoggedInAdmin()
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(2),
                        "[0].email", equalTo("admin@gmail.com"),
                        "[1].email", equalTo("user@gmail.com"));
    }

    @Test
    void getAllUsers_shouldReturn401_whenUnauthorized() {
        get("/api/users")
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void getAllUsers_shouldReturn403_whenUserRole() {
        givenLoggedInUser()
                .when()
                .get("/api/users")
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void getUserById_shouldReturn200_whenValidRequest() {
        RegistrationDto payload = TestDataFactory.createRegistrationDto();

        Integer userId = registerTestUser(payload);

        givenLoggedInAdmin()
                .when()
                .get("/api/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId),
                        "name", equalTo(payload.getName()),
                        "email", equalTo(payload.getEmail()));
    }

    @Test
    void getUserById_shouldReturn401_whenUnauthorized() {
        get("/api/users/987654321")
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void getUserById_shouldReturn403_whenUserRole() {
        givenLoggedInUser()
                .when()
                .get("/api/users/987654321")
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void getUserById_shouldReturn404_whenUserNotFound() {
        givenLoggedInAdmin()
                .when()
                .get("/api/users/987654321")
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("User with id='987654321' not found"));
    }

    @Test
    void updateUser_shouldReturn200_whenValidRequest() {
        RegistrationDto payload = TestDataFactory.createRegistrationDto();

        Integer userId = registerTestUser(payload);

        UserDto userToUpdate = TestDataFactory.createUserDto("USER");
        userToUpdate.setEmail("updatedemail@example.com");

        givenLoggedInAdmin()
                .when()
                .body(userToUpdate)
                .put("/api/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId),
                        "name", equalTo(userToUpdate.getName()),
                        "email", equalTo(userToUpdate.getEmail()));
    }

    @Test
    void updateUser_shouldReturn400_whenInvalidRequest() {
        RegistrationDto payload = TestDataFactory.createRegistrationDto();

        Integer userId = registerTestUser(payload);

        UserDto userToUpdate = TestDataFactory.createUserDto("USER");
        userToUpdate.setEmail(null);

        givenLoggedInAdmin()
                .when()
                .body(userToUpdate)
                .put("/api/users/" + userId)
                .then()
                .statusCode(400)
                .body("status", equalTo(400),
                        "timestamp", notNullValue(),
                        "message", equalTo(EMPTY_EMAIL));
    }

    @Test
    void updateUser_shouldReturn401_whenUnauthorized() {
        UserDto userToUpdate = TestDataFactory.createUserDto("USER");

        put("/api/users/987654321", userToUpdate)
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void updateUser_shouldReturn403_whenUserRole() {
        UserDto userToUpdate = TestDataFactory.createUserDto("USER");

        givenLoggedInUser()
                .when()
                .body(userToUpdate)
                .put("/api/users/987654321")
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void updateUser_shouldReturn404_whenUserNotFound() {
        UserDto userToUpdate = TestDataFactory.createUserDto("USER");

        givenLoggedInAdmin()
                .when()
                .body(userToUpdate)
                .put("/api/users/987654321")
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("User with id='987654321' not found"));
    }

    @Test
    void updateUser_shouldReturn409_whenEmailAlreadyExists() {
        RegistrationDto payload = TestDataFactory.createRegistrationDto();

        Integer userId = registerTestUser(payload);

        UserDto userToUpdate = TestDataFactory.createUserDto("USER");
        userToUpdate.setEmail("admin@gmail.com");

        givenLoggedInAdmin()
                .when()
                .body(userToUpdate)
                .put("/api/users/" + userId)
                .then()
                .statusCode(409)
                .body("status", equalTo(409),
                        "timestamp", notNullValue(),
                        "message", equalTo("User with email='admin@gmail.com' is already in use"));
    }

    @Test
    void deleteUser_shouldReturn200_whenValidRequest() {
        RegistrationDto payload = TestDataFactory.createRegistrationDto();

        Integer userId = registerTestUser(payload);

        givenLoggedInAdmin()
                .when()
                .delete("/api/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId),
                        "email", equalTo(payload.getEmail()));
    }

    @Test
    void deleteUser_shouldReturn401_whenUnauthorized() {
        delete("/api/users/987654321")
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void deleteUser_shouldReturn403_whenUserRole() {
        givenLoggedInUser()
                .when()
                .delete("/api/users/987654321")
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void deleteUser_shouldReturn404_whenUserNotFound() {
        givenLoggedInAdmin()
                .when()
                .delete("/api/users/987654321")
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("User with id='987654321' not found"));
    }

    private Integer registerTestUser(RegistrationDto payload) {
        return post("/api/auth/register", payload)
                .then()
                .statusCode(200)
                .extract().jsonPath().getInt("id");
    }
}
