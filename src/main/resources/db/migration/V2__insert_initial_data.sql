INSERT INTO movies (name, rating_count, average_rating) VALUES
('The Shawshank Redemption', 2, 10.0000),
('The Godfather', 2, 9.5000),
('The Dark Knight', 1, 9.0000),
('Pulp Fiction', 2, 8.5000),
('Forrest Gump', 1, 8.0000),
('Inception', 2, 9.0000),
('Fight Club', 1, 8.0000),
('The Matrix', 2, 9.5000),
('Goodfellas', 2, 9.0000),
('The Lord of the Rings: The Fellowship of the Ring', 1, 9.0000),
('Star Wars: Episode V - The Empire Strikes Back', 2, 9.5000),
('Interstellar', 1, 9.0000),
('The Green Mile', 2, 8.5000),
('Gladiator', 1, 8.0000),
('The Lion King', 2, 9.0000),
('Saving Private Ryan', 1, 7.0000);


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
