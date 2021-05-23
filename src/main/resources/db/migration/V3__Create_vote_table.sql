CREATE TABLE IF NOT EXISTS votes (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    voting_session_id INT NOT NULL,
    vote_value VARCHAR(3) NOT NULL,
    voted_at TIMESTAMP NOT NULL,
    UNIQUE (user_id, voting_session_id),
    CONSTRAINT fk_voting_session FOREIGN KEY(voting_session_id) REFERENCES voting_sessions(id)
);