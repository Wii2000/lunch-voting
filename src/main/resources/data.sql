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

INSERT INTO dish (restaurant_id, name, price_in_cents)
VALUES (1, 'Dish1', 375),
       (1, 'Dish2', 450),
       (1, 'Dish3', 350),

       (2, 'Dish4', 250),
       (2, 'Dish5', 340),
       (2, 'Dish6', 550),

       (3, 'Dish7', 330),
       (3, 'Dish8', 400),
       (3, 'Dish9', 360);