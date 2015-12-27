CREATE TABLE bean (
  id              INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1),
  boolean_field   BOOLEAN NOT NULL,
  int_field       INTEGER NOT NULL,
  string_field    INTEGER          DEFAULT NULL,
  int_value_field INTEGER          DEFAULT NULL
);


