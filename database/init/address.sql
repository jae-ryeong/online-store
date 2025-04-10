/*drop table if exists address;

create table address
(
    address_id bigint not null auto_increment primary key,
    user_id bigint not null ,
    addressee_name varchar(20) not null ,
    postal_code INTEGER not null ,
    tel varchar(30),
    address varchar(200) not null ,
    detail_address varchar(200) not null
) engine = InnoDB;

INSERT INTO address (user_id, addressee_name, postal_code, tel, address, detail_address)
VALUES
    (1, '김철수', 12345, '010-1234-5678', '서울특별시 강남구 테헤란로 123', '101호'),
    (2, '이영희', 54321, '010-9876-5432', '부산광역시 해운대구 해운대로 456', '202호'),
    (3, '박민수', 67890, NULL, '대구광역시 중구 중앙대로 789', '303호'),
    (4, '정수진', 13579, '010-5555-6666', '광주광역시 서구 무진대로 159', '404호'),
    (5, '최다연', 24680, '010-7777-8888', '대전광역시 유성구 대학로 321', '505호');*/