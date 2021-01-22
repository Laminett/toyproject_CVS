INSERT INTO user (id, username, password, department, full_name, email, phone_number, role, created_date, modified_date)
VALUES
(400, 'testtest100', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', '400_fullName', '400@test.com', null, 'ADMIN', null, null),
(401, 'testtest200', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', '401_fullName', '401@test.com', null, 'ADMIN', null, null),
(999, 'forUnitTest', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Mobile Div', '999_fullName', '999@test.com', null, 'ADMIN', null, null)
;

INSERT INTO point (id, created_date, modified_date, point, user_id)
VALUES (400, '2020-09-25 04:21:51', '2020-09-25 04:21:51', 120, 400),
       (999, '2020-09-25 04:21:51', '2020-09-25 04:21:51', 10000, 999);

INSERT INTO point_history (id, created_date, point, user_id)
VALUES (90, '2020-10-05 13:21:51', 500, 400);

INSERT INTO settle (id, created_date, aggregated_at, approval_amount, approval_count, cancel_amount, cancel_count, total_amount, total_count, user_id)
VALUES (90, '2020-10-05 13:21:51', '2020-10-01',  8500, 12, 1200, 2, 7300, 14, 400);

INSERT INTO product_category(id, name, is_enabled, admin_id, created_date, modified_date)
VALUES
(500, 'categorytest', true, 'testid', '2020-10-15 16:00:00', '2020-10-16 16:00:00'),
(501, 'categorytest1', false, 'testid', '2020-10-15 16:00:00', '2020-10-16 16:00:00');

INSERT INTO product(id, name, barcode, is_enabled, point, quantity, category_id, admin_id, created_date, modified_date)
VALUES (500, 'testProduct', '123123123123', true, 100, 200, 500, 'testAdmin', '2020-12-20 12:12:12', '2020-12-20 12:12:12'),
       (501, 'testProduct1', '123123123124', true, 200, 20, 500, 'testAdmin', '2020-12-20 12:12:12', '2020-12-20 12:12:12'),
       (502, 'testProduct2', '123123123125', true, 300, 30, 500, 'testAdmin', '2020-12-20 12:12:12', '2020-12-20 12:12:12');

INSERT INTO product_purchase(id, product_id, category_id, purchase_amount, purchase_quantity, purchase_date, created_date, modified_date, admin_id)
VALUES (500, 500, 500, 500, 50, '2020-12-03', '2020-12-03 12:12:12', '2020-12-03 12:12:12', 'testid');

INSERT INTO transaction (id, created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id)
VALUES (500, '2021-01-08 08:08:08', '2021-01-08 08:08:08', null, 'POS_QR', 100, 'AAAAAAAAAAaaaaaaaaaa', 'SUCCESS', 'PAYMENT', 400),
       (550, '2021-01-08 08:08:08', '2021-01-08 08:08:08', null, 'POS_QR', 100, 'BBBBBBBBBBbbbbbbbbbb', 'WAIT', 'PAYMENT', 400);

INSERT INTO transaction_detail (id, product_id, product_quantity, request_id, created_date, modified_date)
VALUES (500, 500, 10, 'AAAAAAAAAAaaaaaaaaaa', '2021-01-8 09:09:09', '2021-01-8 09:09:09'),
       (501, 501, 10, 'AAAAAAAAAAaaaaaaaaaa', '2021-01-8 09:09:09', '2021-01-8 09:09:09'),
       (502, 502, 10, 'AAAAAAAAAAaaaaaaaaaa', '2021-01-8 09:09:09', '2021-01-8 09:09:09');

INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 07:42:51', '2020-09-22 08:18:29', 1, null, 100, 'jgGZRQuJZubGGIx5thwO', 'SUCCESS', 'PAYMENT', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 07:46:55', '2020-09-22 08:26:03', 2, null, 100, 'jLVLNV6rCIzcO0Gb0OVw', 'SUCCESS', 'PAYMENT', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:00:00', '2020-09-22 08:16:21', null, 'POS_QR', 100, 'JQ83PpkZa8jz5Cl1E4ZR', 'SUCCESS', 'PAYMENT', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:18:29', '2020-09-22 08:18:29', 1, null, 100, 'jgGZRQuJZubGGIx5thwO', 'REFUND', 'REFUND', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:26:03', '2020-09-22 08:26:03', 2, null, 100, 'jLVLNV6rCIzcO0Gb0OVw', 'REFUND', 'REFUND', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:27:13', '2020-09-22 08:27:13', 3, null, 100, 'JQ83PpkZa8jz5Cl1E4ZR', 'REFUND', 'REFUND', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:35:57', '2020-09-22 08:42:58', null, 'POS_QR', 100, 'twFXwz7KPBvs5wC2HKkQ', 'SUCCESS', 'PAYMENT', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 02:34:29', '2020-09-23 02:34:43', null, 'POS_QR', 100, 'z19TFGc5boG78fjmRHQH', 'SUCCESS', 'PAYMENT', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:31:36', '2020-09-23 05:33:44', null, 'POS_QR', 100, 'JV3C3ULGDnuCLBY1qgMi', 'SUCCESS', 'PAYMENT', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:36:11', '2020-09-23 05:36:15', null, 'POS_QR', 100, 'cwBlMGB6k9ctoRVgpQoF', 'SUCCESS', 'PAYMENT', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:37:13', '2020-09-23 05:37:17', null, 'POS_QR', 100, 'iLbYero0fFDmYnm1aNW5', 'SUCCESS', 'PAYMENT', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:41:57', '2020-09-23 05:41:57', 7, null, 100, 'twFXwz7KPBvs5wC2HKkQ', 'REFUND', 'REFUND', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:42:03', '2020-09-23 05:42:03', 8, null, 100, 'z19TFGc5boG78fjmRHQH', 'REFUND', 'REFUND', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:42:07', '2020-09-23 05:42:07', 11, null, 100, 'iLbYero0fFDmYnm1aNW5', 'REFUND', 'REFUND', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:42:15', '2020-09-23 05:42:18', null, 'POS_QR', 100, 'cpGuLRPe3NqLuwIu3mJy', 'SUCCESS', 'PAYMENT', 400);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:42:15', '2020-09-23 05:42:18', null, 'POS_QR', 100, 'cpGuLRPe3NqLuwIu3mJy', 'SUCCESS', 'PAYMENT', 400);

INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 07:42:51', '2020-09-22 08:18:29', 1, null, 200, 'jgGZRQuJZubGGIx5thwO', 'SUCCESS', 'PAYMENT', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 07:46:55', '2020-09-22 08:26:03', 2, null, 100, 'jLVLNV6rCIzcO0Gb0OVw', 'SUCCESS', 'PAYMENT', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:00:00', '2020-09-22 08:16:21', null, 'POS_QR', 100, 'JQ83PpkZa8jz5Cl1E4ZR', 'SUCCESS', 'PAYMENT', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:18:29', '2020-09-22 08:18:29', 1, null, 200, 'jgGZRQuJZubGGIx5thwO', 'REFUND', 'REFUND', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:26:03', '2020-09-22 08:26:03', 2, null, 100, 'jLVLNV6rCIzcO0Gb0OVw', 'REFUND', 'REFUND', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:27:13', '2020-09-22 08:27:13', 3, null, 100, 'JQ83PpkZa8jz5Cl1E4ZR', 'REFUND', 'REFUND', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-22 08:35:57', '2020-09-22 08:42:58', null, 'POS_QR', 100, 'twFXwz7KPBvs5wC2HKkQ', 'SUCCESS', 'PAYMENT', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 02:34:29', '2020-09-23 02:34:43', null, 'POS_QR', 300, 'z19TFGc5boG78fjmRHQH', 'SUCCESS', 'PAYMENT', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:31:36', '2020-09-23 05:33:44', null, 'POS_QR', 100, 'JV3C3ULGDnuCLBY1qgMi', 'SUCCESS', 'PAYMENT', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:36:11', '2020-09-23 05:36:15', null, 'POS_QR', 100, 'cwBlMGB6k9ctoRVgpQoF', 'SUCCESS', 'PAYMENT', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:37:13', '2020-09-23 05:37:17', null, 'POS_QR', 100, 'iLbYero0fFDmYnm1aNW5', 'SUCCESS', 'PAYMENT', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:41:57', '2020-09-23 05:41:57', 7, null, 100, 'twFXwz7KPBvs5wC2HKkQ', 'REFUND', 'REFUND', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:42:03', '2020-09-23 05:42:03', 8, null, 100, 'z19TFGc5boG78fjmRHQH', 'REFUND', 'REFUND', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:42:07', '2020-09-23 05:42:07', 11, null, 100, 'iLbYero0fFDmYnm1aNW5', 'REFUND', 'REFUND', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:42:15', '2020-09-23 05:42:18', null, 'POS_QR', 100, 'cpGuLRPe3NqLuwIu3mJy', 'SUCCESS', 'PAYMENT', 401);
INSERT INTO transaction (created_date, modified_date, origin_request_id, payment_type, point, request_id, state, type, user_id) VALUES ('2020-09-23 05:42:15', '2020-09-23 05:42:18', null, 'POS_QR', 100, 'cpGuLRPe3NqLuwIu3mJy', 'SUCCESS', 'PAYMENT', 401);