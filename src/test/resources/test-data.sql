INSERT INTO user (id, username, password, department, full_name, email, phone_number, role, created_date, modified_date)
VALUES
(400, 'testtest100', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', '400_fullName', '400@test.com', null, 'ADMIN', null, null),
(401, 'testtest200', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', '401_fullName', '401@test.com', null, 'ADMIN', null, null);

INSERT INTO point (id, created_date, modified_date, point, user_id)
VALUES (400, '2020-09-25 04:21:51', '2020-09-25 04:21:51', 120, 400);

INSERT INTO product_category(id, name, is_enabled, admin_id, created_date, modified_date)
VALUES (500, 'categorytest', true, 'testid', '2020-10-15 16:00:00', '2020-10-16 16:00:00');