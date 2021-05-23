CREATE TABLE IF NOT EXISTS voting_sessions (
    id SERIAL PRIMARY KEY,
    subject_id INT NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_subject FOREIGN KEY(subject_id) REFERENCES subjects(id)
);