drop table if exists users;

create table users(
    user_id bigint not null auto_increment primary key,
    user_name varchar(255) not null unique,
    password varchar(255) not null,
    store_name varchar(255),
    role_type enum('ADMIN', 'CUSTOMER', 'SELLER') not null

) engine=InnoDB;

insert into users(user_name, password, role_type)  values ('customer', '1234', 'CUSTOMER');

insert into users(user_name, password, store_name, role_type) values ('seller', '1234', '회사', 'SELLER');
insert into users(user_name, password, store_name, role_type) values ('seller2', '1234', '회사2', 'SELLER');
insert into users(user_name, password, store_name, role_type) values ('seller3', '1234', '회사3', 'SELLER');