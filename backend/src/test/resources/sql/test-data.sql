-- Clean up first
DELETE FROM tickets;
DELETE FROM bookings;
DELETE FROM movie_genres;
DELETE FROM genres;
DELETE FROM sessions;
DELETE FROM seats;
DELETE FROM halls;
DELETE FROM users;
DELETE FROM roles;
DELETE FROM movies;

-- init roles table
INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');

-- init users table
INSERT INTO users(role_id, name, email, password)
VALUES ((SELECT id FROM roles WHERE name = 'ADMIN'), 'admin', 'admin@gmail.com', '$2a$10$ooLnRdwpGFBuTyLPLPxz5ub9zQlQbsw0GSjGdfLdIbMqTlQqYCO0u'),
       ((SELECT id FROM roles WHERE name = 'USER'), 'user', 'user@gmail.com', '$2a$10$bt.5EA5nJUlPR5A1CsBBP.ZeBGjTJqWOjulubwHZMk1fMZAsYF8eO');

-- init movies table
INSERT INTO movies (title, description, duration, age_rating, release_date, poster_url, created_at)
VALUES ('Captain America: Brave New World', 'After meeting with newly elected President of the United States Thaddeus Ross, Sam finds himself at the center of an international scandal. He must uncover the purpose of a deceitful global conspiracy before its true leader makes the whole world turn red with anger.',
        121, '12+', '2025-02-13', 'http://localhost:8080/api/movies/poster/captain_america_brave_new_world.jpg', CURRENT_TIMESTAMP),
       ('Mickey 17', '“Disposable employee” takes part in the colonization of the frozen world. After Miki''s death, his consciousness is loaded into a new body each time.',
        149, '16+', '2025-03-06', 'http://localhost:8080/api/movies/poster/mickey_17.webp', CURRENT_TIMESTAMP);

-- init genres table
INSERT INTO genres (name)
VALUES ('Action'),
       ('Drama'),
       ('Adventure'),
       ('Fantasy'),
       ('Sci-Fi');

-- init movie_genres
INSERT INTO movie_genres (movie_id, genre_id)
VALUES ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT id FROM genres WHERE name = 'Action')),
       ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT id FROM genres WHERE name = 'Fantasy')),
       ((SELECT id FROM movies WHERE title = 'Mickey 17'), (SELECT id FROM genres WHERE name = 'Action')),
       ((SELECT id FROM movies WHERE title = 'Mickey 17'), (SELECT id FROM genres WHERE name = 'Sci-Fi')),
       ((SELECT id FROM movies WHERE title = 'Mickey 17'), (SELECT id FROM genres WHERE name = 'Drama'));


-- init halls table
INSERT INTO halls (name, row_count, seats_per_row)
VALUES ('Big Hall 1', 5, 14),
       ('VIP Hall 2', 3, 4);

-- init seats table
DO
'
    DECLARE
        hall     RECORD;
        row_num  INT;
        seat_num INT;
    BEGIN
        FOR hall IN (SELECT id, row_count, seats_per_row FROM halls)
            LOOP
                FOR row_num IN 1..hall.row_count
                    LOOP
                        FOR seat_num IN 1..hall.seats_per_row
                            LOOP
                                INSERT INTO seats (hall_id, row_number, seat_number)
                                VALUES (hall.id, row_num, seat_num);
                            END LOOP;
                    END LOOP;
            END LOOP;
    END;
'  LANGUAGE PLPGSQL;

INSERT INTO sessions (movie_id, hall_id, start_time, price)
VALUES
    -- Movie 1
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT id FROM halls WHERE name = 'Big Hall 1'), NOW()::DATE + INTERVAL '10:30' HOUR TO MINUTE, 10),
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT id FROM halls WHERE name = 'VIP Hall 2'), NOW()::DATE + INTERVAL '16:20' HOUR TO MINUTE, 10),
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT id FROM halls WHERE name = 'Big Hall 1'), NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '12:30' HOUR TO MINUTE, 10),
    ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT id FROM halls WHERE name = 'VIP Hall 2'), NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '15:20' HOUR TO MINUTE, 10),

    -- Movie 2
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), (SELECT id FROM halls WHERE name = 'Big Hall 1'), NOW()::DATE + INTERVAL '10:30' HOUR TO MINUTE, 15),
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), (SELECT id FROM halls WHERE name = 'VIP Hall 2'), NOW()::DATE + INTERVAL '16:20' HOUR TO MINUTE, 15),
    ((SELECT id FROM movies WHERE title = 'Mickey 17'), (SELECT id FROM halls WHERE name = 'Big Hall 1'), NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '12:30' HOUR TO MINUTE, 15);

-- Insert booking with a fixed ID (-1)
INSERT INTO bookings (id, session_id, name, email, phone, status, total_price, created_at)
VALUES (
           -1,
           (SELECT id FROM sessions LIMIT 1),
           'Test User',
           'testemail@example.com',
           '+380991234567',
           'PAID',
           25,
           CURRENT_TIMESTAMP
       ),
       (
           -2,
           (SELECT id FROM sessions LIMIT 1),
           'Test User',
           'testemail@example.com',
           '+380991234567',
           'PENDING',
           25,
           CURRENT_TIMESTAMP
       );

-- Insert two tickets using explicit seat IDs
INSERT INTO tickets (id, booking_id, session_id, seat_id, used)
SELECT
    -1,
    -1,
    s.id,
    seat1.id,
    FALSE
FROM
    sessions s
        JOIN seats seat1 ON seat1.hall_id = s.hall_id
LIMIT 1;

INSERT INTO tickets (id, booking_id, session_id, seat_id, used)
SELECT
    -2,
    -2,
    s.id,
    seat2.id,
    FALSE
FROM
    sessions s
        JOIN (
        SELECT id, hall_id
        FROM seats
        WHERE hall_id = (SELECT sessions.hall_id FROM sessions LIMIT 1)
        ORDER BY id
        OFFSET 1 LIMIT 1
    ) seat2 ON seat2.hall_id = s.hall_id
LIMIT 1;
