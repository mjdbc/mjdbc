CREATE TABLE custom_types (
  boolean_field     BOOLEAN     DEFAULT NULL,
  big_decimal_field DECIMAL     DEFAULT NULL,
  char_field        CHAR(10)    DEFAULT NULL,
  char_field_nn     CHAR(10)    NOT NULL,
  character_field   INTEGER     DEFAULT NULL,
  byte_field        SMALLINT    DEFAULT NULL,
  date_field        DATE        DEFAULT NULL,
  double_field      DOUBLE      DEFAULT NULL,
  float_field       FLOAT       DEFAULT NULL,
  integer_field     INTEGER     DEFAULT NULL,
  integer_field_nn  INTEGER     NOT NULL,
  long_field        BIGINT      DEFAULT NULL,
  short_field       INTEGER     DEFAULT NULL,
  time_field        TIME        DEFAULT NULL,
  timestamp_field   TIMESTAMP   DEFAULT NULL,
  instant_field     TIMESTAMP   DEFAULT NULL,
  varchar_field     VARCHAR(10) DEFAULT NULL,
  varchar_field_nn  VARCHAR(10) NOT NULL
);


INSERT INTO
  custom_types (
    boolean_field,
    big_decimal_field,
    byte_field,
    char_field, char_field_nn,
    character_field,
    date_field,
    double_field,
    float_field,
    integer_field, integer_field_nn,
    long_field,
    short_field,
    time_field,
    timestamp_field,
    instant_field,
    varchar_field, varchar_field_nn
  )
VALUES (
  TRUE,
  1,
  1,
  NULL, 'value',
  1,
  '2015-09-17',
  1,
  1,
  NULL, 1,
  1,
  1,
  '16:07',
  '2015-09-17 15:50:05', -- timestamp
  '2015-10-17 15:50:05', -- instant
  NULL, 'value'
);



