CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    enabled BOOLEAN DEFAULT TRUE
);

MERGE INTO users KEY(username) VALUES
  (1, 'admin', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5/1WfNfZb3F4iAfDdc0AGJi/7/0u', 'admin@example.com', TRUE),
  (2, 'user1', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5/1WfNfZb3F4iAfDdc0AGJi/7/0u', 'user1@example.com', TRUE);
