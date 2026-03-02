-- Flyway Migration V1: Initial schema for all entities

CREATE TABLE tbl_accounts
(
    id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'customer',
    email      VARCHAR(100) NOT NULL UNIQUE,
    phone      VARCHAR(15) UNIQUE,
    status     VARCHAR(10)  NOT NULL DEFAULT 'active',
    created_at TIMESTAMP(3)          DEFAULT CURRENT_TIMESTAMP(3),
    updated_at TIMESTAMP(3)          DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)
);

CREATE TABLE tbl_users
(
    id         CHAR(36) PRIMARY KEY,
    f_name     VARCHAR(255) NOT NULL,
    l_name     VARCHAR(255) NOT NULL,
    img        VARCHAR(255),
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    acc_id     INT UNSIGNED,
    CONSTRAINT fk_users_account FOREIGN KEY (acc_id) REFERENCES tbl_accounts (id) ON DELETE SET NULL
);
CREATE INDEX idx_users_acc_id ON tbl_users (acc_id);

CREATE TABLE tbl_address
(
    id       INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id  CHAR(36),
    country  VARCHAR(20)  NOT NULL,
    province VARCHAR(20)  NOT NULL,
    district VARCHAR(20)  NOT NULL,
    street   VARCHAR(100) NOT NULL,
    detail   VARCHAR(255),
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES tbl_users (id) ON DELETE CASCADE
);
CREATE INDEX idx_address_user ON tbl_address (user_id);

CREATE TABLE tbl_categories
(
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    category_code VARCHAR(10) NOT NULL unique,
    img_url       VARCHAR(255),
    description   VARCHAR(255),
    status        VARCHAR(20) NOT NULL DEFAULT 'active',
    parent_id     INT UNSIGNED,
    created_at    TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP(3),
    updated_at    TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    version       BIGINT      NOT NULL DEFAULT 0,
    is_leaf       BOOLEAN     NOT NULL DEFAULT TRUE,
    level         INT         NOT NULL DEFAULT 0,
    path          VARCHAR(255),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES tbl_categories (id) ON DELETE SET NULL
);
CREATE INDEX idx_category_status ON tbl_categories (status);
CREATE INDEX idx_category_parent ON tbl_categories (parent_id);
CREATE INDEX idx_category_path ON tbl_categories (path);

CREATE TABLE tbl_providers
(
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    provider_code VARCHAR(10) NOT NULL unique,
    description   VARCHAR(255),
    email         VARCHAR(100),
    phone         VARCHAR(15),
    status        VARCHAR(20) NOT NULL DEFAULT 'active',
    created_at    TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP(3),
    updated_at    TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    logo          VARCHAR(255)
);

CREATE TABLE tbl_products
(
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    product_code  VARCHAR(10) NOT NULL unique,
    description   VARCHAR(255),
    quantity      int unsigned         DEFAULT 0,
    quantity_sold int unsigned         DEFAULT 0,
    price         DECIMAL(19, 2)       DEFAULT 0,
    status        VARCHAR(20) NOT NULL DEFAULT 'active',
    rated         decimal(2, 1),
    created_at    TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP(3),
    updated_at    TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    category_id   INT UNSIGNED,
    provider_id   INT UNSIGNED,
    version       BIGINT      NOT NULL DEFAULT 0,
    img           VARCHAR(255),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES tbl_categories (id),
    CONSTRAINT fk_product_provider FOREIGN KEY (provider_id) REFERENCES tbl_providers (id)
);
CREATE INDEX idx_product_status ON tbl_products (status);
CREATE INDEX idx_product_category ON tbl_products (category_id);
CREATE INDEX idx_product_provider ON tbl_products (provider_id);
CREATE INDEX idx_product_status_price ON tbl_products (status, price);
CREATE INDEX idx_product_status_rated ON tbl_products (status, rated);
CREATE INDEX idx_product_sold ON tbl_products (quantity_sold);

CREATE TABLE tbl_promotions
(
    id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    type            VARCHAR(20)    NOT NULL,
    value           INT            NOT NULL,
    start_at        TIMESTAMP(3)            DEFAULT CURRENT_TIMESTAMP(3),
    end_at          TIMESTAMP(3),
    priority        INT                     DEFAULT 1,
    status          VARCHAR(20)    NOT NULL DEFAULT 'active',
    min_price_apply DECIMAL(19, 2) NOT NULL DEFAULT 0,
    scope           VARCHAR(20)    NOT NULL
);
CREATE INDEX idx_promotion_status ON tbl_promotions (status);

CREATE TABLE tbl_promotion_product
(
    product_id   INT UNSIGNED NOT NULL,
    promotion_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (product_id, promotion_id),
    CONSTRAINT fk_promotion_product_product FOREIGN KEY (product_id) REFERENCES tbl_products (id),
    CONSTRAINT fk_promotion_product_promotion FOREIGN KEY (promotion_id) REFERENCES tbl_promotions (id)
);
CREATE INDEX idx_promotion_product_promotion ON tbl_promotion_product (promotion_id);


CREATE TABLE tbl_promotion_category
(
    promotion_id INT UNSIGNED NOT NULL,
    category_id  INT UNSIGNED NOT NULL,
    PRIMARY KEY (promotion_id, category_id),
    CONSTRAINT fk_promotion_category_promotion
        FOREIGN KEY (promotion_id) REFERENCES tbl_promotions (id) ON DELETE CASCADE,
    CONSTRAINT fk_promotion_category_category
        FOREIGN KEY (category_id) REFERENCES tbl_categories (id) ON DELETE CASCADE
);

CREATE INDEX idx_promotion_category_category
    ON tbl_promotion_category (category_id);