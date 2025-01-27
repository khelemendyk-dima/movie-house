-- users table
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL CHECK (role IN ('ADMIN', 'USER')),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- movies table
CREATE TABLE movies
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    duration     INTEGER      NOT NULL,
    age_rating   VARCHAR(10),
    release_date DATE         NOT NULL,
    poster_url   TEXT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- halls table
CREATE TABLE halls
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    total_seats INTEGER      NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- seats table
CREATE TABLE seats
(
    id          SERIAL PRIMARY KEY,
    hall_id     INTEGER NOT NULL REFERENCES halls (id),
    row_number  INTEGER NOT NULL,
    seat_number INTEGER NOT NULL
);

-- sessions table
CREATE TABLE sessions
(
    id         SERIAL PRIMARY KEY,
    movie_id   INTEGER        NOT NULL REFERENCES movies (id),
    hall_id    INTEGER        NOT NULL REFERENCES halls (id),
    start_time TIMESTAMP      NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
