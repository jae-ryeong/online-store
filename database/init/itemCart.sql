/*drop table if exists item_cart;

create table item_cart
(
    item_cart_id bigint not null auto_increment primary key,
    cart_id bigint not null,
    item_id bigint not null,
    quantity integer not null,
    cart_check boolean not null
) engine = InnoDB;*/