CREATE TABLE author (
    id SERIAL PRIMARY KEY,
    "ФИО" VARCHAR(100) NOT NULL,
    "Дата создания" TIMESTAMP NOT NULL
);

ALTER TABLE budget ADD COLUMN author_id INT REFERENCES author(id)