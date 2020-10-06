INSERT INTO user (id, username, password, department, full_name, email, phone_number, role, created_date, modified_date)
VALUES
(400, 'testtest100', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', '400_fullName', '400@test.com', null, 'ADMIN', null, null),
(401, 'testtest200', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', '401_fullName', '401@test.com', null, 'ADMIN', null, null);

INSERT INTO point (id, created_date, modified_date, point, user_id)
VALUES (400, '2020-09-25 04:21:51', '2020-09-25 04:21:51', 120, 400);

INSERT INTO point_history (id, created_date, point, user_id)
VALUES (90, '2020-10-05 13:21:51', 500, 400);

INSERT INTO settle (id, created_date, aggregated_at, approval_amount, approval_count, cancel_amount, cancel_count, total_amount, total_count, user_id)
VALUES (90, '2020-10-05 13:21:51', '202009',  8500, 12, 1200, 2, 7300, 14, 400);
