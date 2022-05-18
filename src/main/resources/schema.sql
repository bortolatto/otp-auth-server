drop table if exists role;
drop table if exists user;
drop table if exists otp;

create table user
(
    id       bigint auto_increment
        primary key,
    password varchar(255) null,
    username varchar(255) null
);

create table role
(
    id      bigint auto_increment
        primary key,
    name    varchar(255) null,
    user_id bigint       not null,
    constraint FK61g3ambult7v7nh59xirgd9nf
        foreign key (user_id) references user (id)
);

CREATE TABLE IF NOT EXISTS otp
(
    `username` VARCHAR(45) NOT NULL,
    `code`     VARCHAR(45) NULL,
    PRIMARY KEY (`username`)
);

