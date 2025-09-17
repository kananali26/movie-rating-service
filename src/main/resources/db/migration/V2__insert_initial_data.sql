INSERT INTO movies (name, rating_count, average_rating) VALUES
('The Shawshank Redemption', 2, 10),
('The Godfather', 2, 9.5),
('The Dark Knight', 1, 9),
('Pulp Fiction', 2, 8.5),
('Forrest Gump', 1, 8),
('Inception', 2, 9),
('Fight Club', 1, 8),
('The Matrix', 2, 9),
('Goodfellas', 2, 9),
('The Lord of the Rings: The Fellowship of the Ring', 1, 9),
('Star Wars: Episode V - The Empire Strikes Back', 2, 9),
('Interstellar', 1, 9),
('The Green Mile', 2, 8),
('Gladiator', 1, 8),
('The Lion King', 2, 9),
('Saving Private Ryan', 1, 7);


INSERT INTO roles (name) VALUES
 ('ROLE_ADMIN'),
 ('ROLE_USER');

INSERT INTO privileges (name) VALUES
('RATE_MOVIE'),
('UPDATE_MOVIE_RATING'),
('DELETE_MOVIE_RATING'),
('MANAGE_MOVIES');

-- ROLE_ADMIN → RATE_MOVIE, UPDATE_MOVIE_RATING, DELETE_MOVIE_RATING, MANAGE_MOVIES
INSERT INTO roles_privileges (role_id, privilege_id)
SELECT r.id, p.id
FROM roles r
         JOIN privileges p ON p.name IN ('RATE_MOVIE', 'UPDATE_MOVIE_RATING', 'DELETE_MOVIE_RATING', 'MANAGE_MOVIES')
WHERE r.name = 'ROLE_ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM roles_privileges rp
    WHERE rp.role_id = r.id AND rp.privilege_id = p.id
);

-- ROLE_USER → RATE_MOVIE, UPDATE_MOVIE_RATING, DELETE_MOVIE_RATING
INSERT INTO roles_privileges (role_id, privilege_id)
SELECT r.id, p.id
FROM roles r
         JOIN privileges p ON p.name IN ('RATE_MOVIE', 'UPDATE_MOVIE_RATING', 'DELETE_MOVIE_RATING')
WHERE r.name = 'ROLE_USER'
  AND NOT EXISTS (
    SELECT 1 FROM roles_privileges rp
    WHERE rp.role_id = r.id AND rp.privilege_id = p.id
);


-- Insert users
-- Password for all users is: Password123!
INSERT INTO users (email, password)
VALUES
    ('admin1@example.com', '$2a$10$sVGAcPcpWQc6gk/jLULFquK.zCEeJ2KlD/Hkxt.4Ne586S7CdC6Pu'),
    ('admin2@example.com', '$2a$10$sVGAcPcpWQc6gk/jLULFquK.zCEeJ2KlD/Hkxt.4Ne586S7CdC6Pu'),
    ('admin3@example.com', '$2a$10$sVGAcPcpWQc6gk/jLULFquK.zCEeJ2KlD/Hkxt.4Ne586S7CdC6Pu'),
    ('user1@example.com',  '$2a$10$sVGAcPcpWQc6gk/jLULFquK.zCEeJ2KlD/Hkxt.4Ne586S7CdC6Pu'),
    ('user2@example.com',  '$2a$10$sVGAcPcpWQc6gk/jLULFquK.zCEeJ2KlD/Hkxt.4Ne586S7CdC6Pu'),
    ('user3@example.com',  '$2a$10$sVGAcPcpWQc6gk/jLULFquK.zCEeJ2KlD/Hkxt.4Ne586S7CdC6Pu');

-- Map admins to ROLE_ADMIN
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.email IN ('admin1@example.com', 'admin2@example.com', 'admin3@example.com');

-- Map users to ROLE_USER
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ROLE_USER'
WHERE u.email IN ('user1@example.com', 'user2@example.com', 'user3@example.com');
