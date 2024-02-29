drop table if exists orderItems;

create table orderItems
(
    order_item_id    bigint not null auto_increment primary key,
    order_id bigint not null,
    item_id bigint not null,
    order_item_count bigint not null,
    order_price bigint not null
) engine = InnoDB;

