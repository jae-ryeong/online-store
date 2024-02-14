drop table if exists item_cart;

create table item_cart
(
    item_cart_id bigint not null auto_increment primary key,
    cart_id bigint,
    item_id bigint
) engine = InnoDB;