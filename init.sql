CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE tb_role
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_name VARCHAR(255) NOT NULL
);

CREATE TABLE tb_users
(
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name     VARCHAR(50)  NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE tb_user_role
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES tb_users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES tb_role (id) ON DELETE CASCADE
);

CREATE VIEW user_roles_view AS
SELECT u.id AS user_id,
       u.name AS user_name,
       u.email AS user_email,
       r.id AS role_id,
       r.role_name AS role_name
FROM tb_users u
JOIN tb_user_role ur ON u.id = ur.user_id
JOIN tb_role r ON ur.role_id = r.id;