/*drop table if exists address;
drop table if exists cart;
drop table if exists item;
drop table if exists item_cart;
drop table if exists likes;
drop table if exists order_items;
drop table if exists orders;
drop table if exists payment;
drop table if exists review;
drop table if exists users;

create table cart
(
    cart_id      bigint not null auto_increment primary key,
    item_cart_id bigint
);

create table users
(
    user_id    bigint                               not null auto_increment primary key,
    user_name  varchar(255)                         not null unique,
    password   varchar(255)                         not null,
    store_name varchar(255),
    role_type  enum ('ADMIN', 'CUSTOMER', 'SELLER') not null,
    item_id    bigint,
    cart_id    bigint UNIQUE,
    FOREIGN KEY (cart_id) REFERENCES cart (cart_id)
);

create table address
(
    address_id     bigint       not null auto_increment primary key,
    user_id        bigint       not null,
    addressee_name varchar(20)  not null,
    postal_code    INTEGER      not null,
    tel            varchar(30),
    address        varchar(200) not null,
    detail_address varchar(200) not null,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);


create table item
(
    item_id        bigint                                  not null auto_increment primary key,
    item_name      varchar(255)                            not null,
    quantity       integer                                 not null,
    price          integer                                 not null,
    user_id        bigint                                  not null,
    sold_count     bigint                                  not null,
    category       enum ('BOOK', 'FOOD', 'CLOTHES', 'PET') not null,
    sold_out       boolean                                 not null,
    description    TEXT                                    not null,
    main_image_url varchar(512)                            not null,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

create table item_cart
(
    item_cart_id bigint  not null auto_increment primary key,
    cart_id      bigint  not null,
    item_id      bigint  not null,
    quantity     integer not null,
    cart_check   boolean not null,
    FOREIGN KEY (cart_id) REFERENCES cart (cart_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item (item_id) ON DELETE CASCADE
);

create table review
(
    review_id  bigint       not null auto_increment primary key,
    user_id    bigint       not null,
    item_id    bigint       not null,
    content    text         not null,
    created_at datetime     not null,
    created_by varchar(255) not null,
    updated_at datetime     not null,
    FOREIGN KEY (item_id) REFERENCES item (item_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

create table likes
(
    like_id   bigint not null auto_increment primary key,
    user_id   bigint,
    item_id   bigint,
    review_id bigint,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item (item_id) ON DELETE CASCADE,
    FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE
);

create table orders
(
    order_id       bigint         not null auto_increment primary key,
    order_items    bigint,
    order_date     datetime,
    user_id        bigint,
    address_id     bigint         not null,
    payment_status BOOLEAN        not null,
    review         BOOLEAN        not null,
    amount         DECIMAL(10, 2) not null,
    item_name      varchar(255)   not null,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES address (address_id)
);

CREATE TABLE payment
(
    payment_id     varchar(255) PRIMARY KEY,
    order_id       BIGINT UNIQUE,
    user_id        BIGINT,
    order_name     VARCHAR(255),
    amount         DECIMAL(10, 2) NOT NULL,
    payment_status BOOLEAN  DEFAULT FALSE,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

create table order_items
(
    order_item_id    bigint  not null auto_increment primary key,
    order_id         bigint  not null,
    item_id          bigint  not null,
    order_item_count INTEGER not null,
    order_status     enum ('ORDER', 'CANCEL', 'COMPLETE', 'TAKE_BACK_APPLICATION', 'TAKE_BACK_COMPLETE'),
    order_price      INTEGER not null,
    FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item (item_id) ON DELETE CASCADE
);

insert into cart(cart_id) values (1);
insert into cart(cart_id) values (2);
insert into cart(cart_id) values (3);
insert into cart(cart_id) values (4);
insert into cart(cart_id) values (5);
insert into cart(cart_id) values (6);

insert into users(user_name, password, role_type, cart_id)  values ('userA', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'ADMIN', 1);
insert into users(user_name, password, role_type, cart_id)  values ('userB', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'CUSTOMER', 2);
insert into users(user_name, password, role_type, cart_id)  values ('userC', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'SELLER', 3);

INSERT INTO address (user_id, addressee_name, postal_code, tel, address, detail_address)
VALUES
    (1, '김철수', 12345, '010-1234-5678', '서울특별시 강남구 테헤란로 123', '101호'),
    (1, '김민혁', 12589, '010-5678-5678', '부산광역시 강남구 해운대로 456', '아파트'),
    (2, '이영희', 54321, '010-5678-5432', '부산광역시 해운대구 해운대로 456', '202호'),
    (2, '이민지', 51221, '010-1234-5432', '대전광역시 해운대구 해운대로 456', '505호'),
    (2, '이김박', 11321, '010-6666-5432', '광주광역시 해운대구 해운대로 456', '404호'),
    (3, '박민수', 67890, NULL, '대구광역시 중구 중앙대로 789', '303호');

insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('커피', 1000, 9800, false, 3, 'FOOD', 7, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('김치', 100, 18900, false, 3, 'FOOD', 0, '<p><br></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사/eb1d9993-6195-4d83-a2bd-d4a6e914f956.JPG" alt="uploaded image"></p><p><strong>썰은 배추김치</strong></p><p>김치 한 번 썰고 나면, 뚝뚝 흐르는 김칫국물 때문에 참 번거로울 때도 있는데요. 이제 맛있는 김치를&nbsp;<strong>손질 없이 간편하게&nbsp;</strong>즐겨보세요. 아삭한 절임배추에&nbsp;<strong>매콤하면서도 개운한 풍미</strong>가 쏙 배어 들어 있답니다. 자를 필요 없이 바로 접시에 옮겨 담아 식탁에 올려보세요.</p><p><br></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사/77202071-e30e-4b72-8d7f-1ad64741324e.JPG" alt="uploaded image"></p><p><strong>체크 포인트</strong></p><p>안심할 수 있는<strong>&nbsp;HACCP 인증</strong></p>', 'https://storage.googleapis.com/gcs_image_12321/회사/4d77c174-2866-4094-9490-a9276e6449cd.JPG');
*/