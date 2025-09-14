INSERT INTO ROLES (name)
VALUES ('ROLE_ADMIN');
INSERT INTO ROLES (name)
VALUES ('ROLE_USER');

INSERT INTO PRIVILEGES (name)
VALUES ('READ');
INSERT INTO PRIVILEGES (name)
VALUES ('WRITE');

-- ROLE_ADMIN → READ, WRITE
INSERT INTO ROLES_PRIVILEGES (role_id, privilege_id)
SELECT r.id, p.id
FROM ROLES r
         JOIN PRIVILEGES p ON p.name IN ('READ', 'WRITE')
WHERE r.name = 'ROLE_ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM ROLES_PRIVILEGES rp
    WHERE rp.role_id = r.id AND rp.privilege_id = p.id
);

-- ROLE_USER → READ
INSERT INTO ROLES_PRIVILEGES (role_id, privilege_id)
SELECT r.id, p.id
FROM ROLES r
         JOIN PRIVILEGES p ON p.name = 'READ'
WHERE r.name = 'ROLE_USER'
  AND NOT EXISTS (
    SELECT 1 FROM ROLES_PRIVILEGES rp
    WHERE rp.role_id = r.id AND rp.privilege_id = p.id
);