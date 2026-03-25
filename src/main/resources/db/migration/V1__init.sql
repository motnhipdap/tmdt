-- Flyway Migration V1: Initial schema for all entities (PostgreSQL)

CREATE TABLE tbl_accounts
(
    id         SERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(10)  NOT NULL DEFAULT 'CUSTOMER' CHECK (role IN ('CUSTOMER', 'ADMIN', 'EMPLOYEE')),
    email      VARCHAR(100) NOT NULL UNIQUE,
    phone      VARCHAR(15)  UNIQUE,
    status     VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'BANNED')),
    created_at TIMESTAMP(3)          DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(3)          DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_users
(
    id         CHAR(36) PRIMARY KEY,
    f_name     VARCHAR(255) NOT NULL,
    l_name     VARCHAR(255) NOT NULL,
    img        VARCHAR(255),
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    acc_id     INT          NOT NULL UNIQUE,
    CONSTRAINT fk_users_account FOREIGN KEY (acc_id) REFERENCES tbl_accounts (id) ON DELETE CASCADE
);
CREATE INDEX idx_users_acc_id ON tbl_users (acc_id);

CREATE TABLE tbl_address
(
    id       SERIAL PRIMARY KEY,
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
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    code        VARCHAR(10) NOT NULL UNIQUE,
    img_url     VARCHAR(255),
    description VARCHAR(255),
    status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    parent_id   INT,
    created_at  TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP,
    version     BIGINT      NOT NULL DEFAULT 0,
    is_leaf     BOOLEAN     NOT NULL DEFAULT TRUE,
    level       INT         NOT NULL DEFAULT 0,
    path        VARCHAR(255),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES tbl_categories (id) ON DELETE SET NULL
);
CREATE INDEX idx_category_status ON tbl_categories (status);
CREATE INDEX idx_category_parent ON tbl_categories (parent_id);
CREATE INDEX idx_category_path ON tbl_categories (path);

CREATE TABLE tbl_providers
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    code        VARCHAR(10) NOT NULL UNIQUE,
    description VARCHAR(255),
    email       VARCHAR(100),
    phone       VARCHAR(15),
    status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at  TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP,
    version     BIGINT      NOT NULL DEFAULT 0,
    logo        VARCHAR(255)
);

CREATE TABLE tbl_products
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    code          VARCHAR(10) NOT NULL UNIQUE,
    description   VARCHAR(255),
    quantity_sold INT                  DEFAULT 0,
    price         DECIMAL(19, 2)       DEFAULT 0,
    status        VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    rated         DECIMAL(2, 1),
    created_at    TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP(3)         DEFAULT CURRENT_TIMESTAMP,
    category_id   INT,
    provider_id   INT,
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


create table tbl_size(
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    height DECIMAL(5, 2) NOT NULL,
    width DECIMAL(5, 2) NOT NULL
);

create table tbl_items(
    product_id INT NOT NULL,
    size_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    PRIMARY KEY (product_id, size_id),
    CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES tbl_products (id) ON DELETE CASCADE,
    CONSTRAINT fk_item_size FOREIGN KEY (size_id) REFERENCES tbl_size (id) ON DELETE CASCADE
);

CREATE TABLE tbl_promotions
(
    id              SERIAL PRIMARY KEY,
    type            VARCHAR(20)    NOT NULL,
    value           INT            NOT NULL,
    code            VARCHAR(20)    NOT NULL UNIQUE,
    start_at        TIMESTAMP(3)            DEFAULT CURRENT_TIMESTAMP,
    end_at          TIMESTAMP(3),
    priority        INT                     DEFAULT 1,
    status          VARCHAR(20)    NOT NULL DEFAULT 'SCHEDULED',
    min_price_apply DECIMAL(19, 2) NOT NULL DEFAULT 0,
    scope           VARCHAR(20)    NOT NULL DEFAULT 'GLOBAL',
    version         BIGINT         NOT NULL DEFAULT 0
);
CREATE INDEX idx_promotion_status ON tbl_promotions (status);

CREATE TABLE tbl_promotion_product
(
    product_id   INT NOT NULL,
    promotion_id INT NOT NULL,
    PRIMARY KEY (product_id, promotion_id),
    CONSTRAINT fk_promotion_product_product FOREIGN KEY (product_id) REFERENCES tbl_products (id),
    CONSTRAINT fk_promotion_product_promotion FOREIGN KEY (promotion_id) REFERENCES tbl_promotions (id)
);
CREATE INDEX idx_promotion_product_promotion ON tbl_promotion_product (promotion_id);

CREATE TABLE tbl_promotion_category
(
    promotion_id INT NOT NULL,
    category_id  INT NOT NULL,
    PRIMARY KEY (promotion_id, category_id),
    CONSTRAINT fk_promotion_category_promotion FOREIGN KEY (promotion_id) REFERENCES tbl_promotions (id) ON DELETE CASCADE,
    CONSTRAINT fk_promotion_category_category FOREIGN KEY (category_id) REFERENCES tbl_categories (id) ON DELETE CASCADE
);
CREATE INDEX idx_promotion_category_category ON tbl_promotion_category (category_id);

create table tbl_carts
(
    id SERIAL PRIMARY KEY,
    user_id CHAR(36) NOT NULL UNIQUE,
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES tbl_users (id) ON DELETE CASCADE
);
CREATE INDEX idx_cart_user ON tbl_carts (user_id);

CREATE TABLE tbl_cart_items
(
    cart_id    INT NOT NULL,
    product_id INT NOT NULL,
    size_id    INT NOT NULL,
    quantity   INT NOT NULL DEFAULT 1,
    PRIMARY KEY (cart_id, product_id, size_id),
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES tbl_carts (id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES tbl_products (id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_size FOREIGN KEY (size_id) REFERENCES tbl_size (id) ON DELETE CASCADE
);
CREATE INDEX idx_cart_item_product ON tbl_cart_items (product_id);
CREATE INDEX idx_cart_item_size ON tbl_cart_items (size_id);

CREATE TABLE tbl_orders
(
    id         SERIAL PRIMARY KEY,
    user_id    CHAR(36) NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL DEFAULT 0,
    status     VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES tbl_users (id) ON DELETE CASCADE
);
CREATE INDEX idx_order_user ON tbl_orders (user_id);
CREATE INDEX idx_order_status ON tbl_orders (status);