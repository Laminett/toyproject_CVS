# create posts
CREATE TABLE posts
(
    id            bigint AUTO_INCREMENT PRIMARY KEY,
    author        varchar(255) NULL,
    content       text         NOT NULL,
    title         varchar(500) NOT NULL,
    created_date  datetime     NULL,
    modified_date datetime     NULL
);

# create user
CREATE TABLE user
(
    id            bigint AUTO_INCREMENT PRIMARY KEY,
    username      varchar(255) NOT NULL,
    password      varchar(255) NOT NULL,
    department    varchar(255) NOT NULL,
    full_name     varchar(255) NOT NULL,
    email         varchar(255) NULL,
    phone_number  varchar(255) NULL,
    role          varchar(255) NOT NULL,
    created_date  datetime     NULL,
    modified_date datetime     NULL
);



