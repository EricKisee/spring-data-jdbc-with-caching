create table if not exists customer 
(
    id serial primary key,
    name text
);

create table if not exists customer_orders
(
    id serial primary key,
    customer bigint not null references customer (id),
    name varchar(255)
);

create table if not exists customer_profiles
(
    id serial primary key,
    customer bigint not null references customer (id),
    username text not null,
    password text not null
);

delete from customer_orders;
delete from customer_profiles;
delete from customer;