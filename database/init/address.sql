drop table if exists address;

create table address
(
    address_id bigint not null auto_increment primary key,
    user_id bigint not null ,
    addressee_name varchar(20) not null ,
    postal_code INTEGER not null ,
    tel varchar(30),
    address varchar(200) not null ,
    detail_address varchar(200) not null
) engine = InnoDB;