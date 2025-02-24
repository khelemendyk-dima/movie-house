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
    name     VARCHAR(50) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT fk_users_role_id FOREIGN KEY(role_id) REFERENCES roles(id)
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
    poster_url   TEXT
);

INSERT INTO movies(title, description, duration, age_rating, release_date, poster_url) VALUES ('Captain America', 'Some interesting description about captain America', 144, '16+', '2025-02-02', '253525e3-c34f-4e8a-b866-5313808f5e3f_Captain_America_Brave_New_World.jpg');

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
    movie_id   INTEGER        NOT NULL REFERENCES movies (id),
    hall_id    INTEGER        NOT NULL REFERENCES halls (id),
    start_time TIMESTAMP      NOT NULL,
    price      DECIMAL(10, 2) NOT NULL
);

-- orders table
CREATE TABLE orders
(
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER        NOT NULL REFERENCES users (id),
    session_id  INTEGER        NOT NULL REFERENCES sessions (id),
    status      VARCHAR(20)    NOT NULL CHECK (status IN ('RESERVED', 'PAID', 'CANCELLED')),
    total_price DECIMAL(10, 2) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- reserved_seats table
CREATE TABLE reserved_seats
(
    id       SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders (id),
    seat_id  INTEGER NOT NULL REFERENCES seats (id)
);

-- payments table
CREATE TABLE payments
(
    id             SERIAL PRIMARY KEY,
    order_id       INTEGER     NOT NULL REFERENCES orders (id),
    payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('STRIPE', 'PAYPAL')),
    payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('PENDING', 'COMPLETED', 'FAILED')),
    transaction_id VARCHAR(255),
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
