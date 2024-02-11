drop table if exists item;

create table item
(
    item_id    bigint not null auto_increment primary key,
    item_name  varchar(255) not null,
    quantity integer not null,
    price integer not null,
    user_id bigint not null,
    item_count bigint not null,
    category enum('BOOK', 'FOOD', 'CLOTHES', 'PET') not null,
    `like` integer not null,
    sold_out boolean not null
) engine = InnoDB;

insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Pontiac', 1, 1, false, 1, 'PET', 0, 7);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Acura', 2, 2, false, 2, 'FOOD', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Chevrolet', 3, 3, false, 3, 'CLOTHES', 0, 8);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Porsche', 4, 4, false, 1, 'BOOK', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Pontiac', 5, 5, false, 3, 'PET', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Ford', 6, 6, true, 1, 'PET', 10, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Nissan', 7, 7, true, 2, 'BOOK', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Dodge', 8, 8, false, 1, 'PET', 7, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Cadillac', 9, 9, true, 3, 'FOOD', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Audi', 10, 10, true, 1, 'CLOTHES', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Subaru', 11, 11, true, 1, 'PET', 9, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Toyota', 12, 12, false, 2, 'BOOK', 0, 10);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Mazda', 13, 13, false, 1, 'PET', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Volkswagen', 14, 14, true, 1, 'PET', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Cadillac', 15, 15, false, 2, 'CLOTHES', 8, 9);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Volkswagen', 16, 16, true, 1, 'FOOD', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Mitsubishi', 17, 17, true, 3, 'PET', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Mercedes-Benz', 18, 18, true, 1, 'CLOTHES', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('Audi', 19, 19, false, 3, 'PET', 0, 0);
insert into item (item_name, quantity, price, sold_out, user_id, category, `like`, item_count) values ('GMC', 20, 20, true, 2, 'BOOK', 0, 0);

