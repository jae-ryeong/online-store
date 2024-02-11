drop table if exists cart;

create table cart
(
    cart_id bigint not null auto_increment primary key
) engine = InnoDB;