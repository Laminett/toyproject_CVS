INSERT INTO user (id, username, password, department, full_name, email, phone_number, status, role, created_date, modified_date)
VALUES
(1, 'admin', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', 'admin', 'test@test.com', null, 'ACTIVE', 'ADMIN', null, null),
(2, 'test', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', 'test', 'test@test.com', null, 'ACTIVE', 'ADMIN', null, null),
(3, 'shkim', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', 'shkim', 'shkim@alli-ex.com', null, 'ACTIVE', 'USER', null, null)
;

INSERT INTO point (id, user_id, point, created_date, modified_date)
VALUES
(1, 1, 100, '2021-01-11 08:53:54', '2021-01-11 08:53:54'),
(2, 2, 200, '2021-01-11 08:53:54', '2021-01-11 08:53:54'),
(3, 3, 300, '2021-01-11 08:53:54', '2021-01-11 08:53:54')
;
