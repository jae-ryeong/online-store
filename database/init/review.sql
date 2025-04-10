/*drop table if exists review;

create table review
(
    review_id bigint not null auto_increment primary key,
    item_id bigint not null ,
    content text not null ,
    created_at datetime not null ,
    created_by varchar(255) not null ,
    updated_at datetime not null
) engine = InnoDB;*/