CREATE DATABASE IF NOT EXISTS register;

USE register;

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    last_login TIMESTAMP NOT NULL,
    status VARCHAR(50) PRIMARY KEY,
);

CREATE TABLE IF NOT EXISTS phones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    number INT NOT NULL,
    city_code  INT NOT NULL,
    country_code INT NOT NULL,
    FOREIGN KEY (user_email) REFERENCES user(email)
);

CREATE TABLE IF NOT EXISTS session (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    token VARCHAR(255) NOT NULL,
    creation TIMESTAMP NOT NULL,
    expired TIMESTAMP NOT NULL,
    FOREIGN KEY (user_email) REFERENCES user(email)
);

CREATE INDEX idx_session_user_email ON session(user_email);
CREATE INDEX idx_phones_user_id ON phones(user_email);

ALTER TABLE user ADD CONSTRAINT uk_user_email UNIQUE (email);