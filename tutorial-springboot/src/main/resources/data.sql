INSERT INTO category(name) VALUES ('Eurogames');
INSERT INTO category(name) VALUES ('Ameritrash');
INSERT INTO category(name) VALUES ('Familiar');

INSERT INTO author(name, nationality) VALUES ('Alan R. Moon', 'US');
INSERT INTO author(name, nationality) VALUES ('Vital Lacerda', 'PT');
INSERT INTO author(name, nationality) VALUES ('Simone Luciani', 'IT');
INSERT INTO author(name, nationality) VALUES ('Perepau Llistosella', 'ES');
INSERT INTO author(name, nationality) VALUES ('Michael Kiesling', 'DE');
INSERT INTO author(name, nationality) VALUES ('Phil Walker-Harding', 'US');

INSERT INTO game(title, age, category_id, author_id) VALUES ('On Mars', '14', 1, 2);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Aventureros al tren', '8', 3, 1);
INSERT INTO game(title, age, category_id, author_id) VALUES ('1920: Wall Street', '12', 1, 4);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Barrage', '14', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Azul', '8', 3, 5);

INSERT INTO client(name) VALUES ('Alberto Hernandez Martinez');
INSERT INTO client(name) VALUES ('Maria Peña Lopez');
INSERT INTO client(name) VALUES ('Diego Sandoval Chacon');

INSERT INTO loan(begin_date, end_date, client_id, game_id) VALUES ('2024-02-01T14:30:00.000+01:00','2024-02-15T14:30:00.000+01:00', 1, 1);
INSERT INTO loan(begin_date, end_date, client_id, game_id) VALUES ('2024-03-05T14:30:00.000+01:00','2024-06-15T14:30:00.000+01:00', 1, 2);
INSERT INTO loan(begin_date, end_date, client_id, game_id) VALUES ('2025-01-01T14:30:00.000Z','2025-01-15T14:30:00.000Z', 2, 3);
INSERT INTO loan(begin_date, end_date, client_id, game_id) VALUES ('2025-03-01T14:30:00.000Z','2025-03-15T14:30:00.000Z', 3, 6);
INSERT INTO loan(begin_date, end_date, client_id, game_id) VALUES ('2025-03-03T14:30:00.000Z','2025-03-15T14:30:00.000Z', 3, 5);
INSERT INTO loan(begin_date, end_date, client_id, game_id) VALUES ('2025-02-28T14:30:00.000Z','2025-03-03T14:30:00.000Z', 2, 1);