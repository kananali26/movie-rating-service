CREATE TABLE IF NOT EXISTS movies
(
    id                    BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                  VARCHAR(255)   NOT NULL,
    rating_count          BIGINT         NOT NULL DEFAULT 0,
    average_rating        DECIMAL(3, 1) NOT NULL DEFAULT 0.0,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users
(
    id                    BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email                 VARCHAR(50)   NOT NULL,
    password              VARCHAR(255)   NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles
(
    id                    BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                  VARCHAR(255)   NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS privileges
(
    id                    BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                  VARCHAR(255)   NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id     BIGINT       NOT NULL,
    role_id     BIGINT       NOT NULL,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_users_roles_user
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_users_roles_role
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS roles_privileges
(
    role_id        BIGINT       NOT NULL,
    privilege_id   BIGINT       NOT NULL,
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, privilege_id),
    CONSTRAINT fk_roles_privileges_role
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT fk_roles_privileges_priv
    FOREIGN KEY (privilege_id) REFERENCES privileges (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS ratings
(
    id          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    movie_id    BIGINT       NOT NULL,
    rating      BIGINT       NOT NULL,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ratings_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_ratings_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE
);
