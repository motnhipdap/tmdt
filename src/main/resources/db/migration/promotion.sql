create table tbl_promotions
(
    id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    require_id int unsigned not null,
    type       VARCHAR(20)  NOT NULL,
    value      int          NOT NULL,
    start_at   TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    end_at     TIMESTAMP(3),
    priority   int          default 1,
    status     varchar(20)  default 'ACTIVE'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table tbl_promotion_product
(
    id           int unsigned auto_increment primary key,
    product_id   int unsigned not null,
    promotion_id int unsigned not null,
    constraint foreign key (product_id) references tbl_products (id) on delete cascade,
    constraint foreign key (promotion_id) references tbl_promotions (id) on delete cascade
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table tbl_promotion_category
(
    id           int unsigned auto_increment primary key,
    category_id  int unsigned not null,
    promotion_id int unsigned not null,
    constraint foreign key (category_id) references tbl_categories (id) on delete cascade,
    constraint foreign key (promotion_id) references tbl_promotions (id) on delete cascade
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE INDEX idx_pp_promotion ON tbl_promotion_product (promotion_id);

create table tbl_promotion_require
(
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    price_require int unsigned default 0,
    rank_require  int unsigned default 0
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

