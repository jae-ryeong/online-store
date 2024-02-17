drop table if exists cart;

create table cart
(
    cart_id bigint not null auto_increment primary key,
    user_id bigint,
    item_cart_id bigint
) engine = InnoDB;

insert into cart(cart_id, user_id) values (1, 1);
insert into cart(cart_id, user_id) values (2, 2);
insert into cart(cart_id, user_id) values (3, 3);
insert into cart(cart_id, user_id) values (4, 4);
insert into cart(cart_id, user_id) values (5, 5);
insert into cart(cart_id, user_id) values (6, 6);