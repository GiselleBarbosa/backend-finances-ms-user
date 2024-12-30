CREATE VIEW user_roles_view AS
SELECT u.id        AS user_id,
       u.name      AS user_name,
       u.email     AS user_email,
       r.id        AS role_id,
       r.role_name AS role_name
FROM tb_users u
         JOIN
     tb_user_role ur ON u.id = ur.user_id
         JOIN
     tb_role r ON ur.role_id = r.id;
