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
    email         varchar(255) NOT NULL,
    name          varchar(255) NOT NULL,
    picture       varchar(255) NULL,
    role          varchar(255) NOT NULL,
    created_date  datetime     NULL,
    modified_date datetime     NULL
);

