-- User
CREATE TABLE user
(
    id           bigint AUTO_INCREMENT PRIMARY KEY,
    username     varchar(255) NOT NULL,
    password     varchar(255) NOT NULL,
    department   varchar(255) NOT NULL,
    full_name    varchar(255) NOT NULL,
    email        varchar(255) NULL,
    phone_number varchar(255) NULL,
    status       varchar(255) NOT NULL,
    role         varchar(255) NOT NULL,
    created_at   datetime     NULL,
    updated_at   datetime     NULL,
    CONSTRAINT uix_username UNIQUE (username)
);

-- Point
CREATE TABLE point
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    user_id    bigint   NULL,
    point      bigint   NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    CONSTRAINT fk_point_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

-- Point History
CREATE TABLE point_history
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    user_id    bigint       NULL,
    point      bigint       NOT NULL,
    status     varchar(255) NOT NULL,
    admin_id   varchar(255) NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    CONSTRAINT fk_point_history_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

-- Product Category
CREATE TABLE product_category
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    name       varchar(255) NOT NULL,
    is_enabled bit          NOT NULL,
    admin_id   varchar(255) NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    CONSTRAINT uix_name UNIQUE (name)
);

-- Product
CREATE TABLE product
(
    id          bigint AUTO_INCREMENT PRIMARY KEY,
    name        varchar(255) NOT NULL,
    barcode     varchar(255) NOT NULL,
    point       bigint       NOT NULL,
    quantity    int          NOT NULL,
    category_id bigint       NULL,
    is_enabled  bit          NOT NULL,
    admin_id    varchar(255) NULL,
    created_at  datetime     NULL,
    updated_at  datetime     NULL,
    CONSTRAINT uix_barcode UNIQUE (barcode),
    CONSTRAINT fk_product_product_category_id FOREIGN KEY (category_id) REFERENCES product_category (id)
);

CREATE INDEX ix_name ON product (name);

-- Product Purchase
CREATE TABLE product_purchase
(
    id                bigint AUTO_INCREMENT PRIMARY KEY,
    product_id        bigint       NULL,
    category_id       bigint       NULL,
    purchase_amount   bigint       NOT NULL,
    purchase_date     date         NOT NULL,
    purchase_quantity int          NOT NULL,
    admin_id          varchar(255) NULL,
    created_at        datetime     NULL,
    updated_at        datetime     NULL,
    CONSTRAINT fk_product_purchase_product_category_id FOREIGN KEY (category_id) REFERENCES product_category (id),
    CONSTRAINT fk_product_purchase_product_id FOREIGN KEY (product_id) REFERENCES product (id)
);

-- Settle
CREATE TABLE settle
(
    id              bigint AUTO_INCREMENT PRIMARY KEY,
    aggregated_at   date             NOT NULL,
    user_id         bigint           NOT NULL,
    status          varchar(255)     NULL,
    approval_amount bigint DEFAULT 0 NOT NULL,
    approval_count  int    DEFAULT 0 NOT NULL,
    cancel_amount   bigint DEFAULT 0 NOT NULL,
    cancel_count    int    DEFAULT 0 NOT NULL,
    total_amount    bigint DEFAULT 0 NOT NULL,
    total_count     int    DEFAULT 0 NOT NULL,
    admin_id        varchar(255)     NULL,
    created_at      datetime         NULL,
    updated_at      datetime         NULL,
    CONSTRAINT uix_aggregated_at_user_id UNIQUE (aggregated_at, user_id),
    CONSTRAINT fk_settle_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

-- Transaction
CREATE TABLE transaction
(
    id                bigint AUTO_INCREMENT PRIMARY KEY,
    request_id        varchar(255) NOT NULL,
    origin_request_id varchar(255) NULL,
    payment_type      varchar(255) NULL,
    point             bigint       NOT NULL,
    state             varchar(255) NOT NULL,
    type              varchar(255) NOT NULL,
    user_id           bigint       NULL,
    created_at        datetime     NULL,
    updated_at        datetime     NULL,
    CONSTRAINT uix_request_id UNIQUE (request_id),
    CONSTRAINT fk_transaction_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE INDEX ix_origin_request_id ON transaction (origin_request_id);

-- Transaction Detail
CREATE TABLE transaction_detail
(
    id               bigint AUTO_INCREMENT PRIMARY KEY,
    request_id       varchar(255) NOT NULL,
    product_quantity int          NOT NULL,
    product_id       bigint       NULL,
    created_at       datetime     NULL,
    updated_at       datetime     NULL,
    CONSTRAINT fk_transaction_detail_product_id FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE INDEX ix_request_id ON transaction_detail (request_id);
