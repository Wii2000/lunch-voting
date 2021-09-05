INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@mail.com', '{noop}password'),
       ('Admin', 'admin@mail.com', '{noop}admin');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurant (name)
VALUES ('Restaurant1'),
       ('Restaurant2'),
       ('Restaurant3');

INSERT INTO dish (restaurant_id, name, price_in_cents, date)
VALUES (1, 'Dish1', 375, CASE WHEN HOUR(current_time) < 11 THEN CURRENT_DATE ELSE DATEADD('DAY', 1, CURRENT_DATE) END),
       (1, 'Dish2', 450, CASE WHEN HOUR(current_time) < 11 THEN CURRENT_DATE ELSE DATEADD('DAY', 1, CURRENT_DATE) END),
       (1, 'Dish3', 350, CASE WHEN HOUR(current_time) < 11 THEN CURRENT_DATE ELSE DATEADD('DAY', 1, CURRENT_DATE) END),

       (2, 'Dish4', 250, CASE WHEN HOUR(current_time) < 11 THEN CURRENT_DATE ELSE DATEADD('DAY', 1, CURRENT_DATE) END),
       (2, 'Dish5', 340, CASE WHEN HOUR(current_time) < 11 THEN CURRENT_DATE ELSE DATEADD('DAY', 1, CURRENT_DATE) END),
       (2, 'Dish6', 550, CASE WHEN HOUR(current_time) < 11 THEN CURRENT_DATE ELSE DATEADD('DAY', 1, CURRENT_DATE) END),

       (3, 'Dish7', 330, CASE WHEN HOUR(current_time) < 11 THEN CURRENT_DATE ELSE DATEADD('DAY', 1, CURRENT_DATE) END),
       (3, 'Dish8', 400, CASE WHEN HOUR(current_time) < 11 THEN CURRENT_DATE ELSE DATEADD('DAY', 1, CURRENT_DATE) END),
       (3, 'Dish9', 360, CASE WHEN HOUR(current_time) < 11 THEN CURRENT_DATE ELSE DATEADD('DAY', 1, CURRENT_DATE) END);