INSERT IGNORE INTO tb_user (name, email, password) VALUES ('Giselle', 'giselle@email.com', '$2a$10$NYFZ/8WaQ3Qb6FCs.00jce4nxX9w7AkgWVsQCG6oUwTAcZqP9Flqu');

       INSERT IGNORE INTO tb_role (role_name) VALUES ('ROLE_USER');
INSERT IGNORE INTO tb_role (role_name) VALUES ('ROLE_ADMIN');

INSERT IGNORE INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT IGNORE INTO tb_user_role (user_id, role_id) VALUES (1, 2);
