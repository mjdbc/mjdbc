CREATE TABLE users (
  id         INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1),
  login      VARCHAR(30) NOT NULL,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL,
  gender     INTEGER     NOT NULL --gender: 0 - Male, 1 - Female
);


INSERT INTO users (login, first_name, last_name, gender) VALUES ('u1', 'First1', 'Last1', 1);
INSERT INTO users (login, first_name, last_name, gender) VALUES ('u2', 'First2', 'Last2', 0);
