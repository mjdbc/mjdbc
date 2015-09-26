CREATE TABLE users (
  id         INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1),
  login      VARCHAR(30) NOT NULL,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL,
  gender     INTEGER     NOT NULL, --gender: 0 - Male, 1 - Female.
  score      BIGINT      NOT NULL, -- some long integer field to executeUpdate in tests.
  reg_date   TIMESTAMP   NOT NULL
);


INSERT INTO users (login, first_name, last_name, gender, score, reg_date) VALUES ('u1', 'First1', 'Last1', 1, 0, '2015-09-25 18:10:01');
INSERT INTO users (login, first_name, last_name, gender, score, reg_date) VALUES ('u2', 'First2', 'Last2', 0, 0, '2015-09-25 18:10:02');

