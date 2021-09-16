INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@mail.com', '{noop}password'),
       ('Admin', 'admin@mail.com', '{noop}admin'),
       ('User2', 'user2@mail.com', '{noop}password2');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);

INSERT INTO restaurant (name)
VALUES ('Restaurant1'),
       ('Restaurant2'),
       ('Restaurant3');

INSERT INTO dish (restaurant_id, name, price_in_cents, registered)
VALUES (1, 'Dish1', 375, NOW()),
       (1, 'Dish2', 450, NOW()),
       (1, 'Dish3', 350, NOW()),

       (2, 'Dish4', 250, NOW()),
       (2, 'Dish5', 340, NOW()),
       (2, 'Dish6', 550, NOW()),

       (3, 'Dish7', 560, NOW()),
       (3, 'Dish8', 450, NOW()),
       (3, 'Dish9', 590, NOW()),

       (2, 'Dish10', 450, DATEADD('DAY', 1, CURRENT_DATE)),
       (2, 'Dish11', 420, DATEADD('DAY', 1, CURRENT_DATE)),
       (2, 'Dish12', 230, DATEADD('DAY', 1, CURRENT_DATE)),

       (3, 'Dish13', 330, DATEADD('DAY', 1, CURRENT_DATE)),
       (3, 'Dish14', 400, DATEADD('DAY', 1, CURRENT_DATE)),
       (3, 'Dish15', 360, DATEADD('DAY', 1, CURRENT_DATE));

INSERT INTO vote (user_id, restaurant_id, registered)
VALUES (3, 2, NOW());
