CREATE TABLE IF NOT EXISTS MOVIES
(
    id                    BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                  VARCHAR(255)   NOT NULL,
    rating_count          BIGINT         NOT NULL,
    average_rating        DECIMAL(6, 4) NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
