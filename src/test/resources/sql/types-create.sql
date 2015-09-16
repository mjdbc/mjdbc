CREATE TABLE custom_types (
  integer_field    INTEGER     DEFAULT NULL,
  integer_field_nn INTEGER     NOT NULL,
  varchar_field    VARCHAR(30) DEFAULT NULL,
  varchar_field_nn VARCHAR(30) NOT NULL,
  char_field       CHAR(30)    DEFAULT NULL,
  char_field_nn    CHAR(30)    NOT NULL
);


INSERT INTO
  custom_types (integer_field, integer_field_nn, varchar_field, varchar_field_nn, char_field, char_field_nn)
VALUES
  (NULL, 1, NULL, 'value', NULL, 'value');



