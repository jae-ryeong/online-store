drop table if exists orders;

create table orders
(
    order_id    bigint not null auto_increment primary key,
    order_items bigint ,
    order_date datetime,
    user_id bigint not null,
    order_status enum('ORDER', 'CANCEL', 'TAKE_BACK'),
    address_id bigint not null
) engine = InnoDB;

