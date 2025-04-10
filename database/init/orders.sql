/*drop table if exists orders;

create table orders
(
    order_id    bigint not null auto_increment primary key,
    order_items bigint ,
    order_date datetime,
    user_id bigint,
    address_id bigint not null,
    payment_status BOOLEAN not null,
    review BOOLEAN not null ,
    amount DECIMAL(10, 2) not null,
    item_name varchar(255) not null
) engine = InnoDB;

CREATE TABLE payment
(
    payment_id varchar(255) PRIMARY KEY,
    order_id BIGINT UNIQUE ,
    user_id BIGINT,
    order_name VARCHAR(255),
    amount DECIMAL(10, 2) NOT NULL,
    payment_status BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);*/