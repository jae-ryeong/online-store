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
    sold_out boolean not null,
    description TEXT not null,
    main_image_url varchar(512) not null

) engine = InnoDB;

insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Pontiac', 1, 1, false, 4, 'PET', 7, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Acura', 2, 2, false, 5, 'FOOD', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Chevrolet', 3, 3, false, 6, 'CLOTHES', 8, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Porsche', 4, 4, false, 4, 'BOOK',0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Pontiac', 5, 5, false, 5, 'PET', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Ford', 6, 6, true, 6, 'PET', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Nissan', 7, 7, true, 4, 'BOOK', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Dodge', 8, 8, false, 5, 'PET', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Cadillac', 9, 9, true, 6, 'FOOD', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Audi', 10, 10, true, 4, 'CLOTHES', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Subaru', 11, 11, true, 5, 'PET', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Toyota', 12, 12, false, 6, 'BOOK', 10, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Mazda', 13, 13, false, 4, 'PET', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Volkswagen', 14, 14, true, 5, 'PET', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Cadillac', 15, 15, false, 6, 'CLOTHES', 9, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Volkswagen', 16, 16, true, 4, 'FOOD', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Mitsubishi', 17, 17, true, 5, 'PET', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Mercedes-Benz', 18, 18, true, 6, 'CLOTHES', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('Audi', 19, 19, false, 4, 'PET', 0, '', '');
insert into item (item_name, quantity, price, sold_out, user_id, category, item_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', '');

