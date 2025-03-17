-- init roles table
INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');

-- init users table (привязываем роли динамически)
INSERT INTO users(role_id, name, email, password)
VALUES ((SELECT id FROM roles WHERE name = 'ADMIN'), 'admin', 'admin@gmail.com', '$2a$10$ooLnRdwpGFBuTyLPLPxz5ub9zQlQbsw0GSjGdfLdIbMqTlQqYCO0u'),
       ((SELECT id FROM roles WHERE name = 'USER'), 'user', 'user@gmail.com', '$2a$10$bt.5EA5nJUlPR5A1CsBBP.ZeBGjTJqWOjulubwHZMk1fMZAsYF8eO');

-- init movies table
INSERT INTO movies (title, description, duration, age_rating, release_date, poster_url)
VALUES ('Captain America: Brave New World', 'After meeting with newly elected President of the United States Thaddeus Ross, Sam finds himself at the center of an international scandal. He must uncover the purpose of a deceitful global conspiracy before its true leader makes the whole world turn red with anger.',
        121, '12+', '2025-02-13', 'http://localhost:8080/api/movies/poster/captain_america_brave_new_world.jpg'),
       ('Mickey 17', '“Disposable employee” takes part in the colonization of the frozen world. After Miki''s death, his consciousness is loaded into a new body each time.',
        149, '16+', '2025-03-06', 'http://localhost:8080/api/movies/poster/mickey_17.webp'),
       ('The Day the Earth Blew Up: A Looney Tunes Movie', 'Starring Daffy Duck and Porky Pig, the film follows their misadventures as they uncover an alien conspiracy threatening Earth.',
        90, '0+', '2025-03-14', 'http://localhost:8080/api/movies/poster/looney-tunes.jpg'),
       ('Black Bag', 'When intelligence agent Kathryn Woodhouse is suspected of betraying the nation, her husband - also a legendary agent - faces the ultimate test of whether to be loyal to his marriage, or his country.',
        94, '16+', '2025-03-14', 'http://localhost:8080/api/movies/poster/black_bag.webp'),
       ('Snow White', 'Music film adaptation of the classic 1937 movie. The magical musical adventure returns viewers to the story of Snow White and her Stepmother, the Evil Queen.',
        110, '0+', '2025-03-20', 'http://localhost:8080/api/movies/poster/snow_white.webp'),
       ('Attack on Titan the Movie: The Last Attack', 'The fate of the world hangs in the balance as Eren unleashes the ultimate power of the Titans. With a burning determination to eliminate all who threaten Eldia, he leads an unstoppable army of Colossal Titans towards Marley.',
        145, '12+', '2025-03-16', 'http://localhost:8080/api/movies/poster/attack_on_titan.jpg');

-- init genres table
INSERT INTO genres (name)
VALUES ('Action'),
       ('Adventure'),
       ('Animation'),
       ('Comedy'),
       ('Crime'),
       ('Documentary'),
       ('Drama'),
       ('Fantasy'),
       ('Family'),
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

-- init movie_genres
INSERT INTO movie_genres (movie_id, genre_id)
VALUES ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT id FROM genres WHERE name = 'Action')),
       ((SELECT id FROM movies WHERE title = 'Captain America: Brave New World'), (SELECT id FROM genres WHERE name = 'Fantasy')),
       ((SELECT id FROM movies WHERE title = 'Mickey 17'), (SELECT id FROM genres WHERE name = 'Action')),
       ((SELECT id FROM movies WHERE title = 'Mickey 17'), (SELECT id FROM genres WHERE name = 'Sci-Fi')),
       ((SELECT id FROM movies WHERE title = 'Mickey 17'), (SELECT id FROM genres WHERE name = 'Drama')),
       ((SELECT id FROM movies WHERE title = 'The Day the Earth Blew Up: A Looney Tunes Movie'), (SELECT id FROM genres WHERE name = 'Animation')),
       ((SELECT id FROM movies WHERE title = 'The Day the Earth Blew Up: A Looney Tunes Movie'), (SELECT id FROM genres WHERE name = 'Comedy')),
       ((SELECT id FROM movies WHERE title = 'Black Bag'), (SELECT id FROM genres WHERE name = 'Action')),
       ((SELECT id FROM movies WHERE title = 'Black Bag'), (SELECT id FROM genres WHERE name = 'Thriller')),
       ((SELECT id FROM movies WHERE title = 'Snow White'), (SELECT id FROM genres WHERE name = 'Adventure')),
       ((SELECT id FROM movies WHERE title = 'Snow White'), (SELECT id FROM genres WHERE name = 'Family')),
       ((SELECT id FROM movies WHERE title = 'Snow White'), (SELECT id FROM genres WHERE name = 'Fantasy')),
       ((SELECT id FROM movies WHERE title = 'Attack on Titan the Movie: The Last Attack'), (SELECT id FROM genres WHERE name = 'Animation')),
       ((SELECT id FROM movies WHERE title = 'Attack on Titan the Movie: The Last Attack'), (SELECT id FROM genres WHERE name = 'Fantasy')),
       ((SELECT id FROM movies WHERE title = 'Attack on Titan the Movie: The Last Attack'), (SELECT id FROM genres WHERE name = 'Action'));

-- init halls table
INSERT INTO halls (name, row_count, seats_per_row)
VALUES ('Big Hall 1', 7, 16),
       ('Big Hall 2', 5, 14),
       ('VIP Hall 3', 3, 4);

-- init seats table
DO
$$
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
    END
$$;

INSERT INTO sessions (movie_id, hall_id, start_time, price)
VALUES
    -- Movie 1
    (1, 1, NOW()::DATE + INTERVAL '10:30' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (1, 2, NOW()::DATE + INTERVAL '16:20' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (1, 3, NOW()::DATE + INTERVAL '21:00' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (1, 1, NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '12:30' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (1, 2, NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '15:20' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (1, 3, NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '19:10' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),

    -- Movie 2
    (2, 1, NOW()::DATE + INTERVAL '10:30' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (2, 2, NOW()::DATE + INTERVAL '16:20' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (2, 3, NOW()::DATE + INTERVAL '21:00' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (2, 1, NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '12:30' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (2, 2, NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '15:20' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),

    -- Movie 3
    (3, 3, NOW()::DATE + INTERVAL '11:10' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (3, 2, NOW()::DATE + INTERVAL '13:30' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (3, 1, NOW()::DATE + INTERVAL '18:00' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),

    -- Movie 4
    (4, 1, NOW()::DATE + INTERVAL '10:30' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (4, 2, NOW()::DATE + INTERVAL '16:20' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (4, 3, NOW()::DATE + INTERVAL '21:00' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),

    -- Movie 5
    (5, 1, NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '12:30' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (5, 2, NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '15:20' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (5, 3, NOW()::DATE + INTERVAL '1 DAY' + INTERVAL '19:10' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),

    -- Movie 6
    (6, 3, NOW()::DATE + INTERVAL '2 DAY' + INTERVAL '11:10' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (6, 2, NOW()::DATE + INTERVAL '2 DAY' + INTERVAL '13:30' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9)),
    (6, 1, NOW()::DATE + INTERVAL '2 DAY' + INTERVAL '18:00' HOUR TO MINUTE, FLOOR(RANDOM() * 8 + 9));
