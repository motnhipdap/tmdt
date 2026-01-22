create table tbl_accounts (
    id int unsigned auto_increment primary key,
    username varchar(50) unique not null,
    password varchar(255) not null,
    role varchar(10) default 'customer' not null,
    email varchar(100) unique not null,
    phone varchar(10) unique not null,
    status varchar(10) default 'active' not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
);
