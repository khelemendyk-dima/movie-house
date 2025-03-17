-- roles table
CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO roles(NAME) VALUES ('ADMIN');
INSERT INTO roles(NAME) VALUES ('USER');

-- users table
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    role_id  INTEGER      NOT NULL,
    name     VARCHAR(50)  NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT fk_users_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
);

INSERT INTO users(role_id, name, email, password) VALUES (1, 'admin', 'admin@gmail.com', '$2a$10$ooLnRdwpGFBuTyLPLPxz5ub9zQlQbsw0GSjGdfLdIbMqTlQqYCO0u');
INSERT INTO users(role_id, name, email, password) VALUES (2, 'user', 'user@gmail.com', '$2a$10$bt.5EA5nJUlPR5A1CsBBP.ZeBGjTJqWOjulubwHZMk1fMZAsYF8eO');

-- movies table
CREATE TABLE movies
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(100) NOT NULL,
    description  TEXT,
    duration     INTEGER      NOT NULL,
    age_rating   VARCHAR(10),
    release_date DATE         NOT NULL,
    poster_url   TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

INSERT INTO movies(title, description, duration, age_rating, release_date, poster_url) VALUES ('Captain America', 'Some interesting description about captain America', 144, '16+', '2025-02-02', 'http://localhost:8080/api/movies/poster/253525e3-c34f-4e8a-b866-5313808f5e3f_Captain_America_Brave_New_World.jpg');
INSERT INTO movies(title, description, duration, age_rating, release_date, poster_url) VALUES ('Inception', 'A mind-bending thriller', 148, 'PG-13', '2010-07-16', 'http://localhost:8080/api/movies/poster/0ee875f1-be17-48c4-ab73-1132df61f395_iterception.jpg');
INSERT INTO movies(title, description, duration, age_rating, release_date, poster_url) VALUES ('Mickey 17', 'Some interesting description about Mickey 17', 139, '16+', '2025-03-01', 'http://localhost:8080/api/movies/poster/3289b88e-b610-45b4-8915-3cec282ca273_mickey_17.webp');
INSERT INTO movies(title, description, duration, age_rating, release_date, poster_url) VALUES ('The Day the Earth Blew Up: A Looney Tunes Movie', 'Some interesting description about The Day the Earth Blew Up: A Looney Tunes Movie', 90, '0+', '2025-02-25', 'http://localhost:8080/api/movies/poster/f94d685e-5ff7-4e4f-8688-9a8b3ac8c399_looney-tunes.jpg');

CREATE TABLE genres
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO genres (name)
VALUES ('Action'),
       ('Adventure'),
       ('Animation'),
       ('Comedy'),
       ('Crime'),
       ('Documentary'),
       ('Drama'),
       ('Fantasy'),
       ('Historical'),
       ('Horror'),
       ('Musical'),
       ('Mystery'),
       ('Romance'),
       ('Sci-Fi'),
       ('Sports'),
       ('Thriller'),
       ('War'),
       ('Western');

CREATE TABLE movie_genres
(
    movie_id INTEGER NOT NULL REFERENCES movies (id) ON DELETE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES genres (id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, genre_id)
);

INSERT INTO movie_genres(movie_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (1, 8),
       (2, 14),
       (2, 16);

-- halls table
CREATE TABLE halls
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    row_count     INTEGER      NOT NULL,
    seats_per_row INTEGER      NOT NULL
);

INSERT INTO halls(name, row_count, seats_per_row) VALUES ('Big Hall 1', 7, 16);
INSERT INTO halls(name, row_count, seats_per_row) VALUES ('Big Hall 2', 5, 14);
INSERT INTO halls(name, row_count, seats_per_row) VALUES ('VIP Hall 3', 3, 4);

-- seats table
CREATE TABLE seats
(
    id          SERIAL PRIMARY KEY,
    hall_id     INTEGER NOT NULL REFERENCES halls (id),
    row_number  INTEGER NOT NULL,
    seat_number INTEGER NOT NULL
);

-- Hall 1: 7 rows, 16 seats per row
DO $$
DECLARE
row_num INT;
    seat_num INT;
BEGIN
FOR row_num IN 1..7 LOOP
        FOR seat_num IN 1..16 LOOP
            INSERT INTO seats (hall_id, row_number, seat_number)
            VALUES (1, row_num, seat_num);
END LOOP;
END LOOP;
END $$;

-- Hall 2: 5 rows, 14 seats per row
DO $$
DECLARE
row_num INT;
    seat_num INT;
BEGIN
FOR row_num IN 1..5 LOOP
        FOR seat_num IN 1..14 LOOP
            INSERT INTO seats (hall_id, row_number, seat_number)
            VALUES (2, row_num, seat_num);
END LOOP;
END LOOP;
END $$;

-- Hall 3 (VIP): 4 rows, 3 seats per row
DO $$
DECLARE
row_num INT;
    seat_num INT;
BEGIN
FOR row_num IN 1..3 LOOP
        FOR seat_num IN 1..4 LOOP
            INSERT INTO seats (hall_id, row_number, seat_number)
            VALUES (3, row_num, seat_num);
END LOOP;
END LOOP;
END $$;

-- sessions table
CREATE TABLE sessions
(
    id         SERIAL PRIMARY KEY,
    movie_id   INTEGER        NOT NULL REFERENCES movies (id) ON DELETE CASCADE,
    hall_id    INTEGER        NOT NULL REFERENCES halls (id) ON DELETE CASCADE,
    start_time TIMESTAMP      NOT NULL,
    price      DECIMAL(10, 2) NOT NULL
);

-- bookings table
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

-- tickets table
CREATE TABLE tickets
(
    id         SERIAL PRIMARY KEY,
    booking_id INTEGER NOT NULL REFERENCES bookings (id) ON DELETE CASCADE,
    session_id INTEGER NOT NULL REFERENCES sessions (id) ON DELETE CASCADE,
    seat_id    INTEGER NOT NULL REFERENCES seats (id) ON DELETE CASCADE,
    used       BOOLEAN DEFAULT FALSE,
    UNIQUE (session_id, seat_id)
);
