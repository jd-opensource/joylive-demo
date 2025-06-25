create table if not exists orders
(
    id        bigint auto_increment primary key,
    user_code varchar(50)  not null,
    product   varchar(100) not null,
    quantity  int          not null,
    price     double       not null
);

insert into orders (user_code, product, quantity, price) values (1, 'phone', 2, 5047);

create table if not exists user_info
(
    id    bigint auto_increment primary key,
    code  varchar(50)  not null,
    name  varchar(100) not null,
    email varchar(100) null,
    constraint user_code unique (code)
);

insert into user_info (code, name, email) values ('unit1', 'user1', null);
insert into user_info (code, name, email) values ('unit2', 'user2', null);