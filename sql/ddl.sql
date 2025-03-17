CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    role_id  INTEGER      NOT NULL,
    name     VARCHAR(50)  NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT fk_users_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE movies
(
    id            SERIAL PRIMARY KEY,
    title         VARCHAR(100)                        NOT NULL,
    description   TEXT                                NOT NULL,
    duration      INTEGER                             NOT NULL,
    age_rating    VARCHAR(10)                         NOT NULL,
    release_date  DATE                                NOT NULL,
    poster_url    TEXT                                NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE genres
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE movie_genres
(
    movie_id INTEGER NOT NULL REFERENCES movies (id) ON DELETE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES genres (id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, genre_id)
);

CREATE TABLE halls
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    row_count     INTEGER      NOT NULL,
    seats_per_row INTEGER      NOT NULL
);

CREATE TABLE seats
(
    id          SERIAL PRIMARY KEY,
    hall_id     INTEGER NOT NULL REFERENCES halls (id),
    row_number  INTEGER NOT NULL,
    seat_number INTEGER NOT NULL
);

CREATE TABLE sessions
(
    id         SERIAL PRIMARY KEY,
    movie_id   INTEGER        NOT NULL REFERENCES movies (id) ON DELETE CASCADE,
    hall_id    INTEGER        NOT NULL REFERENCES halls (id) ON DELETE CASCADE,
    start_time TIMESTAMP      NOT NULL,
    price      DECIMAL(10, 2) NOT NULL
);

CREATE TABLE bookings
(
    id          SERIAL PRIMARY KEY,
    session_id  INTEGER        NOT NULL REFERENCES sessions (id) ON DELETE CASCADE,
    name        VARCHAR(50)    NOT NULL,
    email       VARCHAR(255)   NOT NULL,
    phone       VARCHAR(20)    NOT NULL,
    status      VARCHAR(20)    NOT NULL CHECK (status IN ('PENDING', 'PAID', 'CANCELLED')),
    total_price DECIMAL(10, 2) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tickets
(
    id         SERIAL PRIMARY KEY,
    booking_id INTEGER NOT NULL REFERENCES bookings (id) ON DELETE CASCADE,
    session_id INTEGER NOT NULL REFERENCES sessions (id) ON DELETE CASCADE,
    seat_id    INTEGER NOT NULL REFERENCES seats (id) ON DELETE CASCADE,
    used       BOOLEAN DEFAULT FALSE,
    UNIQUE (session_id, seat_id)
);
