CREATE TABLE IF NOT EXISTS USERS
(
    id                    BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email                 VARCHAR(50)   NOT NULL,
    password              VARCHAR(255)   NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

