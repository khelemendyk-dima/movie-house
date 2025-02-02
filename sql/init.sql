-- roles table
CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO roles(ID, NAME) VALUES (1, 'ADMIN');
INSERT INTO roles(ID, NAME) VALUES (2, 'USER');

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

INSERT INTO users(id, role_id, name, email, password) VALUES (1, 1, 'admin', 'admin@gmail.com', '$2a$10$7/2PxqDj6TEJhPlWpDQFJeaQko5ZJ9SliaiyFA/5ALGhqJKMIIBxS');
INSERT INTO users(id, role_id, name, email, password) VALUES (2, 2, 'user', 'user@gmail.com', '$2a$10$Pf4LDhp0gqnOrFbdnf4zRuFHEl9vg.vAqmqUcBH5gkV8Fo10Pjgde');

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
