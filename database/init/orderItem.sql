drop table if exists order_items;

create table order_items
(
    order_item_id    bigint not null auto_increment primary key,
    order_id bigint not null,
    item_id bigint not null,
    order_item_count INTEGER not null,
    order_status enum('ORDER', 'CANCEL', 'COMPLETE', 'TAKE_BACK_APPLICATION', 'TAKE_BACK_COMPLETE'),
    order_price INTEGER not null
) engine = InnoDB;

