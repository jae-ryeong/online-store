drop table if exists users;

create table users(
    user_id bigint not null auto_increment primary key,
    user_name varchar(255) not null unique,
    password varchar(255) not null,
    store_name varchar(255),
    role_type enum('ADMIN', 'CUSTOMER', 'SELLER') not null,
    item_id bigint,
    cart_id bigint

) engine=InnoDB;

insert into users(user_name, password, role_type)  values ('admin', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'ADMIN');

insert into users(user_name, password, role_type)  values ('customer', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'CUSTOMER');
insert into users(user_name, password, role_type)  values ('customer2', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'CUSTOMER');

insert into users(user_name, password, store_name, role_type) values ('seller', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', '회사', 'SELLER');
insert into users(user_name, password, store_name, role_type) values ('seller2', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', '회사2', 'SELLER');
insert into users(user_name, password, store_name, role_type) values ('seller3', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', '회사3', 'SELLER');