drop table if exists likes;

create table likes
(
    like_id bigint not null auto_increment primary key,
    user_id bigint,
    item_id bigint,
    review_id bigint
) engine = InnoDB;