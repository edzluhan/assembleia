CREATE TABLE IF NOT EXISTS subjects (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    details TEXT DEFAULT NULL
);