package com.moviehouse.integration;

import com.moviehouse.config.PostgresTestContainer;
import com.moviehouse.dto.LoginDto;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected Integer port;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> container = PostgresTestContainer.getInstance();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
    }

    protected Response get(String path) {
        return given()
                .when()
                .get(path);
    }

    protected Response post(String path, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(path);
    }

    protected Response put(String path, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(path);
    }

    protected Response delete(String path) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(path);
    }

    protected RequestSpecification givenLoggedInUser() {
        return givenAuth(loginAsUser());
    }

    protected RequestSpecification givenLoggedInAdmin() {
        return givenAuth(loginAsAdmin());
    }

    protected RequestSpecification givenAuth(String jwt) {
        return given()
                .cookie("jwt", jwt)
                .contentType(ContentType.JSON);
    }

    protected String loginAndGetJwt(String email, String password) {
        LoginDto loginDto = new LoginDto(email, password);

        return post("/api/auth/login", loginDto)
                .then()
                .statusCode(200)
                .extract()
                .cookie("jwt");
    }

    protected String loginAsUser() {
        return loginAndGetJwt("user@gmail.com", "12345678Aa!");
    }

    protected String loginAsAdmin() {
        return loginAndGetJwt("admin@gmail.com", "12345678Aa!");
    }
}
