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

insert into users(user_name, password, role_type, cart_id)  values ('admin', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'ADMIN', 1);

insert into users(user_name, password, role_type, cart_id)  values ('customer', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'CUSTOMER', 2);
insert into users(user_name, password, role_type, cart_id)  values ('customer2', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'CUSTOMER', 3);

insert into users(user_name, password, store_name, role_type, cart_id) values ('seller', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', '회사', 'SELLER', 4);
insert into users(user_name, password, store_name, role_type, cart_id) values ('seller2', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', '회사2', 'SELLER', 5);
insert into users(user_name, password, store_name, role_type, cart_id) values ('seller3', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', '회사3', 'SELLER', 6);