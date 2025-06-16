package com.moviehouse.integration.controller;

import com.moviehouse.dto.LoginDto;
import com.moviehouse.dto.RegistrationDto;
import com.moviehouse.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_EMAIL;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_PASSWORD;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.INVALID_LOGIN_DATA;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void authRegister_shouldReturn200_whenValidRequest() {
        RegistrationDto payload = new RegistrationDto("Test User", "user@example.com", "Aa12345678!", "Aa12345678!");

        post("/api/auth/register", payload)
                .then()
                .statusCode(200)
                .body("id", notNullValue(),
                        "role", equalTo("USER"),
                        "email", equalTo(payload.getEmail()),
                        "name", equalTo(payload.getName()));
    }

    @Test
    void authRegister_shouldReturn400_whenInvalidRequest() {
        RegistrationDto payload = new RegistrationDto("Test User", null, "Aa12345678!", "Aa12345678!");

        post("/api/auth/register", payload)
                .then()
                .statusCode(400)
                .body("status", equalTo(400),
                        "timestamp", notNullValue(),
                        "message", equalTo(EMPTY_EMAIL));
    }

    @Test
    void authRegister_shouldReturn409_whenEmailInUse() {
        RegistrationDto payload = new RegistrationDto("Test User", "user@example.com", "Aa12345678!", "Aa12345678!");

        post("/api/auth/register", payload)
                .then()
                .statusCode(200);

        // register the same user second time
        post("/api/auth/register", payload)
                .then()
                .statusCode(409)
                .body("status", equalTo(409),
                        "timestamp", notNullValue(),
                        "message", equalTo("User with email='" + payload.getEmail() + "' is already in use"));
    }


    @Test
    void authLogin_shouldReturn200AndJwtCookie_whenValidRequest() {
        LoginDto payload = new LoginDto("user@gmail.com", "12345678Aa!");

        post("/api/auth/login", payload)
                .then()
                .statusCode(200)
                .cookie("jwt", notNullValue())
                .body("token", notNullValue(),
                        "user.email", equalTo(payload.getEmail()));
    }

    @Test
    void authLogin_shouldReturn400_whenInvalidRequest() {
        LoginDto payload = new LoginDto("user@gmail.com", null);

        post("/api/auth/login", payload)
                .then()
                .statusCode(400)
                .body("status", equalTo(400),
                        "timestamp", notNullValue(),
                        "message", equalTo(EMPTY_PASSWORD));
    }

    @Test
    void authLogin_shouldReturn401_whenInvalidCredentials() {
        LoginDto payload = new LoginDto("user@gmail.com", "wrong password");

        post("/api/auth/login", payload)
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo(INVALID_LOGIN_DATA));
    }

    @Test
    void authLogout_shouldReturn200AndResetJwtCookie() {
        givenLoggedInUser()
                .when()
                .post("/api/auth/logout")
                .then()
                .statusCode(200)
                .cookie("jwt", "");
    }

    @Test
    void authMe_shouldReturn200_whenUserLoggedIn() {
        givenLoggedInUser()
                .when()
                .get("/api/auth/me")
                .then()
                .statusCode(200)
                .body("email", equalTo("user@gmail.com"));
    }

    @Test
    void authMe_shouldReturn401_whenUserUnauthorized() {
        get("/api/auth/me")
                .then()
                .statusCode(401);
    }
}
