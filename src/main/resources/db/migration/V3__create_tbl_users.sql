CREATE TABLE tbl_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    acc_id int unsigned default 0,
    name varchar(50) not null,
    addr varchar(255) default null,
    point int unsigned default 0,
    img varchar(255) default null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at timestamp default current_timestamp on update current_timestamp,
    constraint acc_id foreign key (acc_id) references tbl_accounts(id)
    on delete cascade
);
