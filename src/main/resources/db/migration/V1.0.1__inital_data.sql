insert into user(id, email, password, name, lastname, role, status, started_at, created_by)
VALUES (1, 'admin@admin.com', '$2a$10$TZmV98Zg3LV1Jg3sZFMobe9WqX1daWmxYe0ky0mgcwOckeXJYPXxK', 'Admin', 'Admin', 'ADMIN', 'ACTIVE', '2023-01-01', NULL),
       (2, 'user@user.com', '$2a$10$ePpaQxTByX4htE3nXKpHqOvlC20vpGWbiPA292jwz8G.g2HUCuCZW', 'User', 'User', 'USER', 'ACTIVE', '2023-01-01', 1);

insert into holiday(name, day_of_month, month_of_year)
VALUES ('Yılbaşı', 1, 1),
       ('Ramazan Bayramı 1.gün', 21, 4),
       ('Ramazan Bayramı 2.gün', 22, 4),
       ('Ramazan Bayramı 3.gün', 23, 4),
       ('Ulusal Egemenlik ve Çocuk Bayramı', 23, 4),
       ('Emek ve Dayanışma Günü', 1, 5),
       ('Atatürk\'ü Anma, Gençlik ve Spor Bayramı', 19, 5),
       ('Kurban Bayramı 1.gün', 28, 6),
       ('Kurban Bayramı 2.gün', 29, 6),
       ('Kurban Bayramı 3.gün', 30, 6),
       ('Kurban Bayramı 4.gün', 1, 7),
       ('Zafer Bayramı', 30, 8),
       ('Cumhuriyet Bayramı', 29, 10);