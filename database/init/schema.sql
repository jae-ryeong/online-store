drop table if exists address;
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
) engine = InnoDB;

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
) engine = InnoDB;

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
) engine = InnoDB;


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
) engine = InnoDB;

create table item_cart
(
    item_cart_id bigint  not null auto_increment primary key,
    cart_id      bigint  not null,
    item_id      bigint  not null,
    quantity     integer not null,
    cart_check   boolean not null,
    FOREIGN KEY (cart_id) REFERENCES cart (cart_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item (item_id) ON DELETE CASCADE
) engine = InnoDB;

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
) engine = InnoDB;

create table likes
(
    like_id   bigint not null auto_increment primary key,
    user_id   bigint,
    item_id   bigint,
    review_id bigint,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item (item_id) ON DELETE CASCADE,
    FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE
) engine = InnoDB;

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
) engine = InnoDB;

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
) engine = InnoDB;

insert into cart(cart_id) values (1);
insert into cart(cart_id) values (2);
insert into cart(cart_id) values (3);
insert into cart(cart_id) values (4);
insert into cart(cart_id) values (5);
insert into cart(cart_id) values (6);

insert into users(user_name, password, role_type, cart_id)  values ('admin', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'ADMIN', 1);
insert into users(user_name, password, role_type, cart_id)  values ('customer', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'CUSTOMER', 2);
insert into users(user_name, password, role_type, cart_id)  values ('customer2', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', 'CUSTOMER', 3);
insert into users(user_name, password, store_name, role_type, cart_id) values ('seller', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', '회사', 'SELLER', 4);
insert into users(user_name, password, store_name, role_type, cart_id) values ('seller2', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', '회사2', 'SELLER', 5);
insert into users(user_name, password, store_name, role_type, cart_id) values ('seller3', '$2a$10$qMIiQrKqgzFVR8y.IQdZeuNh/W6OK7Hf5xxhvHttyi8wBQG8mM77a', '회사3', 'SELLER', 6);



INSERT INTO address (user_id, addressee_name, postal_code, tel, address, detail_address)
VALUES
    (1, '김철수', 12345, '010-1234-5678', '서울특별시 강남구 테헤란로 123', '101호'),
    (1, '김민혁', 12589, '010-5678-5678', '부산광역시 강남구 해운대로 456', '아파트'),
    (2, '이영희', 54321, '010-5678-5432', '부산광역시 해운대구 해운대로 456', '202호'),
    (2, '이민지', 51221, '010-1234-5432', '대전광역시 해운대구 해운대로 456', '505호'),
    (2, '이김박', 11321, '010-6666-5432', '광주광역시 해운대구 해운대로 456', '404호'),
    (3, '박민수', 67890, NULL, '대구광역시 중구 중앙대로 789', '303호'),
    (4, '정수진', 13579, '010-5555-7777', '서울특별시 서구 무진대로 159', '404호'),
    (4, '최다연', 51221, '010-5678-6666', '광주광역시 서구 무진대로 159', '202호'),
    (5, '최다연', 13579, '010-5555-8888', '대전광역시 해운대로 대학로 321', '505호'),
    (5, '이영희', 24680, '010-7777-6666', '광주광역시 유성구 대학로 321', '50202호');

insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('커피', 1000, 9800, false, 4, 'FOOD', 7, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('김치', 100, 18900, false, 5, 'FOOD', 0, '<p><br></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사/eb1d9993-6195-4d83-a2bd-d4a6e914f956.JPG" alt="uploaded image"></p><p><strong>썰은 배추김치</strong></p><p>김치 한 번 썰고 나면, 뚝뚝 흐르는 김칫국물 때문에 참 번거로울 때도 있는데요. 이제 맛있는 김치를&nbsp;<strong>손질 없이 간편하게&nbsp;</strong>즐겨보세요. 아삭한 절임배추에&nbsp;<strong>매콤하면서도 개운한 풍미</strong>가 쏙 배어 들어 있답니다. 자를 필요 없이 바로 접시에 옮겨 담아 식탁에 올려보세요.</p><p><br></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사/77202071-e30e-4b72-8d7f-1ad64741324e.JPG" alt="uploaded image"></p><p><strong>체크 포인트</strong></p><p>안심할 수 있는<strong>&nbsp;HACCP 인증</strong></p>', 'https://storage.googleapis.com/gcs_image_12321/회사/4d77c174-2866-4094-9490-a9276e6449cd.JPG');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('에어프라이어', 92, 121000, false, 6, 'FOOD', 8, '<p><br></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사/85721708-a396-46d0-a58a-b77d820ac415.JPG" alt="uploaded image"></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사/99141e90-e1cb-4d00-83cf-5654fb94254f.JPG" alt="uploaded image"></p>', 'https://storage.googleapis.com/gcs_image_12321/회사/1f4826b6-c959-46af-a7de-eab638cdd914.JPG');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('맨투맨 티셔츠', 1200, 12800, false, 4, 'CLOTHES',0, '<p><br></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사/2804f212-fad0-4532-b405-fbed9b491963.JPG" alt="uploaded image"></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사/7d22c0c4-b39e-41e5-914b-ed3254b71c9f.JPG" alt="uploaded image"></p><p><br></p>', 'https://storage.googleapis.com/gcs_image_12321/회사/ac28f240-bcf4-4279-84f7-9a1094726135.JPG');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('폴라 맨투맨', 100, 13400, false, 5, 'CLOTHES', 0, '<p><br></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사2/14f7c6a1-5a91-45a6-b1e3-238ab342a000.JPG" alt="uploaded image"></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사2/905425c4-0290-4fbd-a598-470ca331e878.JPG" alt="uploaded image"></p>', 'https://storage.googleapis.com/gcs_image_12321/회사2/9de40196-2f1c-4427-89c2-5443899332d1.JPG');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('강아지 리드줄', 100, 9800, false, 6, 'PET', 0, '<p><br></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사2/f433bfe3-18b7-4617-b02a-eb1b923864b6.JPG" alt="uploaded image"></p><p><img src="https://storage.googleapis.com/gcs_image_12321/회사2/aaede7a2-26a0-4290-b8ea-3a9c5471d18f.JPG" alt="uploaded image"></p>', 'https://storage.googleapis.com/gcs_image_12321/회사2/7fd5fbdc-8be2-45e1-920b-eaa2c1370425.JPG');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('소년이 온다', 100, 12000, true, 4, 'BOOK', 0, '<h4><img src="http://img1a.coupangcdn.com/image/vendor_inventory/images/2017/06/08/15/6/2bba38b5-dadc-48a6-b86b-dda03d0b1644.gif"></h4><p>섬세한 감수성과 치밀한 문장으로 인간 존재의 본질을 탐구해온 작가 한강의 여섯번째 장편소설 『소년이 온다』가 출간되었다. 1980년 광주의 5월을 다뤄 창비문학블로그 ‘창문’에 연재할 당시(2013년 11월~2014년 1월)부터 독자들의 이목을 끌었던 열다섯살 소년의 이야기는 ‘상처의 구조에 대한 투시와 천착의 서사’를 통해 한강만이 풀어낼 수 있는 방식으로 1980년 5월을 새롭게 조명한다.&nbsp;</p><p><br></p><p>한강은 무고한 영혼들의 말을 대신 전하는 듯한 진심 어린 문장들로 어느덧 그 시절을 잊고 무심하게 5.18 이후를 살고 있는 우리에게 묵직한 질문을 던지고, 여전히 5.18의 트라우마를 안고 힘겹게 살아가는 사람들을 위무한다. 『소년이 온다』는 광주민주화운동 당시 계엄군에 맞서 싸우던 중학생 동호를 비롯한 주변 인물들과 그후 남겨진 사람들의 고통받는 내면을 생생하게 그려내고, 당시의 처절한 장면들을 핍진하게 묘사하며 지금 “우리가 ‘붙들어야 할’ 역사적 기억이 무엇인지를 절실하게 환기하고 있다(백지연 평론가).”&nbsp;</p><p><br></p><p>“이 소설을 피해갈 수 없었”고, “이 소설을 통과하지 않고는 어디로도 갈 수 없다고 느꼈”다는 작가 스스로의 고백처럼 이 소설은 소설가 한강의 지금까지의 작품세계를 한단계 끌어올리는, “한강을 뛰어넘은 한강의 소설(신형철 평론가)”이라고 자신 있게 말할 수 있는 작품이다.&nbsp;</p>', 'https://storage.googleapis.com/gcs_image_12321/회사2/f85c148e-c870-46f7-bac0-34cff931d543.JPG');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('사람은 무엇으로 사는가', 7000, 120, false, 5, 'BOOK', 0, '<p><img src="https://storage.googleapis.com/gcs_image_12321/회사2/09b63e8c-fe2a-48fa-b32a-7017e22d43bc.JPG" alt="uploaded image"></p><p>▶ 책 소개</p><p><br></p><p><br></p><p>자살 직전까지 갔던 톨스토이의 삶을 통째로 바꾼 삶의 진리…&nbsp;</p><p>그 깨달음을 오롯이 녹여낸 명품 단편 모음!</p><p><br></p><p>위대한 소설가로 세계적인 명성을 누렸던 시절에도 채울 수 없었던 톨스토이의 마음을 만족하게 한 것은 무엇이었을까? 그 깨달음으로 살아갈 이유를 찾은 후 작품의 색깔까지 완전히 바꾸어 집필한 10편의 명 단편이 현대지성 클래식 제34권, 『사람은 무엇으로 사는가』로 독자들과 만난다.&nbsp;</p><p>인생의 최고 정점이던 51세 무렵, 1879년을 기점으로 톨스토이의 인생은 완전히 달라진다. 사실 그때는 『전쟁과 평화』(1863-1869), 『안나 카레니나』(1873-1877)를 발표한 직후라 문학적인 명성과 창조적인 영감은 최고 수준이었다. 하지만 그는 피할 수 없는 죽음 앞에 선 인생의 허무함을 인식하며 상류층의 삶이 철저히 거짓과 위선 위에 세워졌다는 결론에 이른다.&nbsp;</p><p>신 앞에 단독자로 선 그는, “인간은 왜 사는가?”를 고민하는 과정에서 깨달은 진실을 어린아이와 민중도 이해할 수 있는 동화 형태로 집필하기 시작한다.&nbsp;</p><p>톨스토이는 복음서 속 예수의 말씀을 실생활에서 적용 가능한 행동강령으로 정리하여 이야기 안에 구현했다. 당대 혁명운동의 폭력성과 편협성을 보면서 진정한 변화는 개개인의 변화에서 시작됨을 역설했고, 영혼의 거듭남과 부활이 사회 전체를 변화시키는 힘이라는 것을 강조했다. 인생 최악의 위기 속에서도 “내가 사는 이유”를 진지하게 고민하는 독자에게 이 책은 묵직한 울림을 줄 것이다.&nbsp;</p><p><br></p><p><br></p><p><br></p><p><br></p><p><br></p><p>▶ 목차</p><p><br></p><p><br></p><p>사람은 무엇으로 사는가&nbsp;</p><p>사랑이 있는 곳에 하나님이 있다&nbsp;</p><p>두 노인&nbsp;</p><p>초반에 불길을 잡지 못하면 끌 수가 없다&nbsp;</p><p>촛불&nbsp;</p><p>대자(代子)&nbsp;</p><p>바보 이반&nbsp;</p><p>사람에게는 얼마만한 땅이 필요한가&nbsp;</p><p>노동과 죽음과 질병&nbsp;</p><p>세 가지 질문&nbsp;</p>', 'https://storage.googleapis.com/gcs_image_12321/회사2/29167ba0-d2e8-4259-931b-0c21d0985410.JPG');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 9, 9, true, 6, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 10, 10, true, 4, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Subaru', 11, 11, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Toyota', 12, 12, false, 6, 'BOOK', 10, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mazda', 13, 13, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 14, 14, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 15, 15, false, 6, 'CLOTHES', 9, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 16, 16, true, 4, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mitsubishi', 17, 17, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mercedes-Benz', 18, 18, true, 6, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 19, 19, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Pontiac', 1, 1, false, 4, 'PET', 7, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Acura', 2, 2, false, 5, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Chevrolet', 3, 3, false, 6, 'CLOTHES', 8, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Porsche', 4, 4, false, 4, 'BOOK',0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Pontiac', 5, 5, false, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Ford', 6, 6, true, 6, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Nissan', 7, 7, true, 4, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Dodge', 8, 8, false, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 9, 9, true, 6, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 10, 10, true, 4, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Subaru', 11, 11, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Toyota', 12, 12, false, 6, 'BOOK', 10, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mazda', 13, 13, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 14, 14, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 15, 15, false, 6, 'CLOTHES', 9, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 16, 16, true, 4, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mitsubishi', 17, 17, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mercedes-Benz', 18, 18, true, 6, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 19, 19, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Pontiac', 1, 1, false, 4, 'PET', 7, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Acura', 2, 2, false, 5, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Chevrolet', 3, 3, false, 6, 'CLOTHES', 8, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Porsche', 4, 4, false, 4, 'BOOK',0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Pontiac', 5, 5, false, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Ford', 6, 6, true, 6, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Nissan', 7, 7, true, 4, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Dodge', 8, 8, false, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 9, 9, true, 6, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 10, 10, true, 4, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Subaru', 11, 11, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Toyota', 12, 12, false, 6, 'BOOK', 10, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mazda', 13, 13, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 14, 14, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 15, 15, false, 6, 'CLOTHES', 9, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 16, 16, true, 4, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mitsubishi', 17, 17, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mercedes-Benz', 18, 18, true, 6, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 19, 19, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');

insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Pontiac', 1, 1, false, 4, 'PET', 7, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Acura', 2, 2, false, 5, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Chevrolet', 3, 3, false, 6, 'CLOTHES', 8, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Porsche', 4, 4, false, 4, 'BOOK',0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Pontiac', 5, 5, false, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Ford', 6, 6, true, 6, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Nissan', 7, 7, true, 4, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Dodge', 8, 8, false, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 9, 9, true, 6, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 10, 10, true, 4, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Subaru', 11, 11, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Toyota', 12, 12, false, 6, 'BOOK', 10, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mazda', 13, 13, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 14, 14, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 15, 15, false, 6, 'CLOTHES', 9, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 16, 16, true, 4, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mitsubishi', 17, 17, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mercedes-Benz', 18, 18, true, 6, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 19, 19, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');

insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Pontiac', 1, 1, false, 4, 'PET', 7, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Acura', 2, 2, false, 5, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Chevrolet', 3, 3, false, 6, 'CLOTHES', 8, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Porsche', 4, 4, false, 4, 'BOOK',0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Pontiac', 5, 5, false, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Ford', 6, 6, true, 6, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Nissan', 7, 7, true, 4, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Dodge', 8, 8, false, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 9, 9, true, 6, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 10, 10, true, 4, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Subaru', 11, 11, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Toyota', 12, 12, false, 6, 'BOOK', 10, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mazda', 13, 13, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 14, 14, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Cadillac', 15, 15, false, 6, 'CLOTHES', 9, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Volkswagen', 16, 16, true, 4, 'FOOD', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mitsubishi', 17, 17, true, 5, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Mercedes-Benz', 18, 18, true, 6, 'CLOTHES', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('Audi', 19, 19, false, 4, 'PET', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
insert into item (item_name, quantity, price, sold_out, user_id, category, sold_count, description, main_image_url) values ('GMC', 20, 20, true, 5, 'BOOK', 0, '', 'https://storage.googleapis.com/gcs_image_12321/회사/5d1adce6-854f-4bf4-bd04-993bfa0b9d6a.jpg');
