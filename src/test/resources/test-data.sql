INSERT INTO user (id, username, password, department, full_name, email, phone_number, role, created_date, modified_date)
VALUES
(400, 'testtest100', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', '400_fullName', '400@test.com', null, 'ADMIN', null, null),
(401, 'testtest200', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', '401_fullName', '401@test.com', null, 'ADMIN', null, null),
(402, 'test400', '1234qwer', 'Mobile Div', '402_fullName', '402@test.com', null, 'ADMIN', null, null);

INSERT INTO point (id, created_date, modified_date, point, user_id)
VALUES (400, '2020-09-25 04:21:51', '2020-09-25 04:21:51', 120, 400);

/*INSERT INTO point_history (id, created_date, point, user_id)
VALUES (90, '2020-10-05 13:21:51', 500, 400);*/

INSERT INTO settle (id, created_date, aggregated_at, approval_amount, approval_count, cancel_amount, cancel_count, total_amount, total_count, user_id)
VALUES (90, '2020-10-05 13:21:51', '202009',  8500, 12, 1200, 2, 7300, 14, 400);

INSERT INTO product_category(id, name, is_enabled, admin_id, created_date, modified_date)
VALUES (500, 'categorytest', true, 'testid', '2020-10-15 16:00:00', '2020-10-16 16:00:00');

INSERT INTO product(id, barcode, is_enabled, name, point, quantity, category_id, created_date, modified_date, admin_id)
VALUES (500, '123123123123', true, 'testproduct', 5000, 500, 500, '2020-12-16 12:12:12', '2020-12-16 12:12:12', 'testid');

INSERT INTO product_purchase(id, product_id, category_id, purchase_amount, purchase_quantity, purchase_date, created_date, modified_date, admin_id)
VALUES (500, 500, 500, 5000, 500, '2020-12-03', '2020-12-03 12:12:12', '2020-12-03 12:12:12', 'testid');


