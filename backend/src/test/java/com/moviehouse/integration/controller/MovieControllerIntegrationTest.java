package com.moviehouse.integration.controller;

import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.MovieDto;
import com.moviehouse.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.ACCESS_DENIED;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_TITLE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MovieControllerIntegrationTest extends BaseIntegrationTest {
    @Test
    void getAllMovies_shouldReturn200_whenValidRequest() {
        get("/api/movies")
                .then()
                .statusCode(200)
                .body("$", hasSize(2),
                        "[0].title", equalTo("Captain America: Brave New World"),
                        "[1].title", equalTo("Mickey 17"));
    }

    @Test
    void getAllMovies_shouldReturn200_whenFilteredByDate() {
        get("/api/movies?date=" + LocalDate.now())
                .then()
                .statusCode(200)
                .body("$", hasSize(2),
                        "[0].title", equalTo("Captain America: Brave New World"),
                        "[1].title", equalTo("Mickey 17"));
    }

    @Test
    void getMovieById_shouldReturn200_whenValidRequest() {
        MovieDto movieDto = createMovie();

        get("/api/movies/" + movieDto.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(movieDto.getId().intValue()),
                        "title", equalTo(movieDto.getTitle()),
                        "description", equalTo(movieDto.getDescription()),
                        "releaseDate", equalTo(movieDto.getReleaseDate().toString()),
                        "posterUrl", equalTo(movieDto.getPosterUrl()),
                        "genres", hasSize(2));
    }

    @Test
    void getMovieById_shouldReturn404_whenMovieNotFound() {
        String movieId = "987654321";

        get("/api/movies/" + movieId)
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("Movie with id='987654321' not found"));
    }

    @Test
    void createMovie_shouldReturn200_whenValidRequest() {
        MovieDto movieDto = TestDataFactory.createMovieDto();

        givenLoggedInAdmin()
                .body(movieDto)
                .when()
                .post("/api/movies")
                .then()
                .statusCode(200)
                .body("id", notNullValue(),
                        "title", equalTo(movieDto.getTitle()),
                        "description", equalTo(movieDto.getDescription()),
                        "releaseDate", equalTo(movieDto.getReleaseDate().toString()),
                        "posterUrl", equalTo(movieDto.getPosterUrl()),
                        "genres", hasSize(2));
    }

    @Test
    void createMovie_shouldReturn400_whenInvalidRequest() {
        MovieDto movieDto = TestDataFactory.createMovieDto();
        movieDto.setTitle(null);

        givenLoggedInAdmin()
                .body(movieDto)
                .when()
                .post("/api/movies")
                .then()
                .statusCode(400)
                .body("status", equalTo(400),
                        "timestamp", notNullValue(),
                        "message", equalTo(EMPTY_TITLE));
    }

    @Test
    void createMovie_shouldReturn401_whenUnauthorizedRequest() {
        MovieDto movieDto = TestDataFactory.createMovieDto();

        post("/api/movies", movieDto)
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void createMovie_shouldReturn403_whenUserRole() {
        MovieDto movieDto = TestDataFactory.createMovieDto();

        givenLoggedInUser()
                .body(movieDto)
                .when()
                .post("/api/movies")
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void updateMovie_shouldReturn200_whenValidRequest() {
        MovieDto movieDto = createMovie();
        movieDto.setTitle("Captain America: Brave New World");

        givenLoggedInAdmin()
                .body(movieDto)
                .when()
                .put("/api/movies/" + movieDto.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(movieDto.getId().intValue()),
                        "title", equalTo(movieDto.getTitle()),
                        "description", equalTo(movieDto.getDescription()),
                        "releaseDate", equalTo(movieDto.getReleaseDate().toString()),
                        "posterUrl", equalTo(movieDto.getPosterUrl()),
                        "genres", hasSize(2));
    }

    @Test
    void updateMovie_shouldReturn400_whenValidRequest() {
        MovieDto movieDto = createMovie();
        movieDto.setTitle(null);

        givenLoggedInAdmin()
                .body(movieDto)
                .when()
                .put("/api/movies/" + movieDto.getId())
                .then()
                .statusCode(400)
                .body("status", equalTo(400),
                        "timestamp", notNullValue(),
                        "message", equalTo(EMPTY_TITLE));
    }

    @Test
    void updateMovie_shouldReturn401_whenValidRequest() {
        MovieDto movieDto = createMovie();
        movieDto.setTitle("Captain America: Brave New World");

        put("/api/movies/" + movieDto.getId(), movieDto)
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void updateMovie_shouldReturn403_whenUserRole() {
        MovieDto movieDto = createMovie();
        movieDto.setTitle("Captain America: Brave New World");

        givenLoggedInUser()
                .body(movieDto)
                .when()
                .put("/api/movies/" + movieDto.getId())
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void updateMovie_shouldReturn404_whenMovieNotFound() {
        MovieDto movieDto = TestDataFactory.createMovieDto();
        movieDto.setId(987654321L);

        givenLoggedInAdmin()
                .body(movieDto)
                .when()
                .put("/api/movies/" + movieDto.getId())
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("Movie with id='987654321' not found"));
    }

    @Test
    void deleteMovie_shouldReturn200_whenValidRequest() {
        MovieDto movieDto = createMovie();

        givenLoggedInAdmin()
                .body(movieDto)
                .when()
                .delete("/api/movies/" + movieDto.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(movieDto.getId().intValue()),
                        "title", equalTo(movieDto.getTitle()));
    }

    @Test
    void deleteMovie_shouldReturn401_whenUnauthorizedRequest() {
        MovieDto movieDto = createMovie();

        given()
                .body(movieDto)
                .when()
                .delete("/api/movies/" + movieDto.getId())
                .then()
                .statusCode(401)
                .body("status", equalTo(401),
                        "timestamp", notNullValue(),
                        "message", equalTo("Full authentication is required to access this resource"));
    }

    @Test
    void deleteMovie_shouldReturn403_whenUserRole() {
        MovieDto movieDto = createMovie();

        givenLoggedInUser()
                .body(movieDto)
                .when()
                .delete("/api/movies/" + movieDto.getId())
                .then()
                .statusCode(403)
                .body("status", equalTo(403),
                        "timestamp", notNullValue(),
                        "message", equalTo(ACCESS_DENIED));
    }

    @Test
    void deleteMovie_shouldReturn404_whenMovieNotFound() {
        MovieDto movieDto = createMovie();
        movieDto.setId(987654321L);

        givenLoggedInAdmin()
                .body(movieDto)
                .when()
                .delete("/api/movies/" + movieDto.getId())
                .then()
                .statusCode(404)
                .body("status", equalTo(404),
                        "timestamp", notNullValue(),
                        "message", equalTo("Movie with id='987654321' not found"));
    }

    private MovieDto createMovie() {
        MovieDto movieToCreate = TestDataFactory.createMovieDto();

        return givenLoggedInAdmin()
                .body(movieToCreate)
                .when()
                .post("/api/movies")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(MovieDto.class);
    }
}
