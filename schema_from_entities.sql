-- PostgreSQL schema generated from current JPA entities.
-- Source of truth: src/main/java/com/dev/dungcony/**/entities

CREATE TABLE tbl_accounts (
    id          SERIAL PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(10) NOT NULL DEFAULT 'CUSTOMER',
    email       VARCHAR(100) NOT NULL UNIQUE,
    verify      BOOLEAN NOT NULL DEFAULT FALSE,
    status      VARCHAR(10) NOT NULL DEFAULT 'ACTIVE',
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_users (
    id          CHAR(36) PRIMARY KEY,
    f_name      VARCHAR(255) NOT NULL,
    l_name      VARCHAR(255) NOT NULL,
    avatar      VARCHAR(255),
    acc_id      INT,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_recivers (
    id      SERIAL PRIMARY KEY,
    f_name  VARCHAR(100),
    l_name  VARCHAR(100),
    phone   VARCHAR(10) NOT NULL,
    user_id CHAR(36) NOT NULL,
    CONSTRAINT fk_receivers_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id)
);

CREATE TABLE tbl_address (
    receiver_id INT PRIMARY KEY,
    country     VARCHAR(20) NOT NULL,
    province    VARCHAR(20) NOT NULL,
    district    VARCHAR(20) NOT NULL,
    street      VARCHAR(100) NOT NULL,
    detail      VARCHAR(255),
    CONSTRAINT fk_address_receiver
        FOREIGN KEY (receiver_id) REFERENCES tbl_recivers (id) ON DELETE CASCADE
);

CREATE TABLE tbl_categories (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    code        VARCHAR(10) NOT NULL,
    img_url     VARCHAR(255),
    description VARCHAR(50),
    status      VARCHAR(20) DEFAULT 'ACTIVE',
    parent_id   INT,
    is_leaf     BOOLEAN NOT NULL DEFAULT TRUE,
    level       INT NOT NULL DEFAULT 0,
    path        VARCHAR(255),
    version     BIGINT NOT NULL DEFAULT 0,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_category_parent
        FOREIGN KEY (parent_id) REFERENCES tbl_categories (id) ON DELETE SET NULL
);

CREATE TABLE tbl_providers (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    code        VARCHAR(10) NOT NULL,
    description VARCHAR(255),
    email       VARCHAR(100),
    phone       VARCHAR(15),
    status      VARCHAR(20) DEFAULT 'ACTIVE',
    logo        VARCHAR(255),
    version     BIGINT NOT NULL DEFAULT 0,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_products (
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(50) NOT NULL,
    code           VARCHAR(30) NOT NULL,
    description    VARCHAR(255),
    quantity_sold  INT DEFAULT 0,
    price          DECIMAL(19, 2) DEFAULT 0,
    status         VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    rated          REAL,
    category_id    INT,
    provider_id    INT,
    version        BIGINT NOT NULL DEFAULT 0,
    img            VARCHAR(255),
    video          VARCHAR(255),
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES tbl_categories (id),
    CONSTRAINT fk_product_provider
        FOREIGN KEY (provider_id) REFERENCES tbl_providers (id)
);

CREATE TABLE tbl_sizes (
    id          SERIAL PRIMARY KEY,
    size        VARCHAR(255),
    weight      DOUBLE PRECISION,
    height      DOUBLE PRECISION,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_items (
    product_id  INT NOT NULL,
    size_id     INT NOT NULL,
    quantity    INT NOT NULL DEFAULT 0,
    status      VARCHAR(255) NOT NULL,
    PRIMARY KEY (product_id, size_id),
    CONSTRAINT fk_item_product
        FOREIGN KEY (product_id) REFERENCES tbl_products (id) ON DELETE CASCADE,
    CONSTRAINT fk_item_size
        FOREIGN KEY (size_id) REFERENCES tbl_sizes (id) ON DELETE CASCADE
);

CREATE TABLE tbl_cart_items (
    user_id     CHAR(36) NOT NULL,
    product_id  INT NOT NULL,
    size_id     INT NOT NULL,
    quantity    INT NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, product_id, size_id),
    CONSTRAINT fk_cart_item_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id),
    CONSTRAINT fk_cart_item_product
        FOREIGN KEY (product_id) REFERENCES tbl_products (id),
    CONSTRAINT fk_cart_item_size
        FOREIGN KEY (size_id) REFERENCES tbl_sizes (id)
);

CREATE TABLE tbl_promotions (
    id               SERIAL PRIMARY KEY,
    code             VARCHAR(20) NOT NULL,
    value            INT NOT NULL,
    start_at         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    end_at           TIMESTAMP WITH TIME ZONE,
    priority         INT DEFAULT 1,
    status           VARCHAR(20) DEFAULT 'SCHEDULED',
    scope            VARCHAR(20) NOT NULL DEFAULT 'GLOBAL',
    version          BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE tbl_promotion_product (
    product_id    INT NOT NULL,
    promotion_id  INT NOT NULL,
    PRIMARY KEY (product_id, promotion_id),
    CONSTRAINT fk_promotion_product_product
        FOREIGN KEY (product_id) REFERENCES tbl_products (id),
    CONSTRAINT fk_promotion_product_promotion
        FOREIGN KEY (promotion_id) REFERENCES tbl_promotions (id)
);

CREATE TABLE tbl_promotion_category (
    category_id   INT NOT NULL,
    promotion_id  INT NOT NULL,
    PRIMARY KEY (category_id, promotion_id),
    CONSTRAINT fk_promotion_category_category
        FOREIGN KEY (category_id) REFERENCES tbl_categories (id),
    CONSTRAINT fk_promotion_category_promotion
        FOREIGN KEY (promotion_id) REFERENCES tbl_promotions (id) ON DELETE CASCADE
);

CREATE TABLE tbl_vouchers (
    id                SERIAL PRIMARY KEY,
    code              VARCHAR(30) NOT NULL UNIQUE,
    type              VARCHAR(20) NOT NULL,
    value             INT NOT NULL,
    min_order_amount  DECIMAL(19, 2) NOT NULL DEFAULT 0,
    start_at          TIMESTAMP WITH TIME ZONE,
    end_at            TIMESTAMP WITH TIME ZONE,
    status            VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version           BIGINT NOT NULL DEFAULT 0,
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_user_vouchers (
    voucher_id        INT NOT NULL,
    user_id           CHAR(36) NOT NULL,
    status            VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    used_at           TIMESTAMP WITH TIME ZONE,
    used_order_code   VARCHAR(20),
    version           BIGINT NOT NULL DEFAULT 0,
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (voucher_id, user_id),
    CONSTRAINT uk_user_voucher UNIQUE (user_id, voucher_id),
    CONSTRAINT fk_user_voucher_voucher
        FOREIGN KEY (voucher_id) REFERENCES tbl_vouchers (id),
    CONSTRAINT fk_user_voucher_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id)
);

CREATE TABLE tbl_orders (
    id                SERIAL PRIMARY KEY,
    code              VARCHAR(20) NOT NULL UNIQUE,
    status            VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    voucher_code      VARCHAR(30),
    total_price       DECIMAL(19, 2) NOT NULL,
    voucher_discount  DECIMAL(19, 2),
    final_price       DECIMAL(19, 2) NOT NULL,
    note              VARCHAR(500),
    version           BIGINT NOT NULL DEFAULT 0,
    user_id           CHAR(36) NOT NULL,
    receiver_id       INT NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id),
    CONSTRAINT fk_order_receiver
        FOREIGN KEY (receiver_id) REFERENCES tbl_recivers (id)
);

CREATE TABLE tbl_order_items (
    order_id      INT NOT NULL,
    product_id    INT NOT NULL,
    size_id       INT NOT NULL,
    quantity      INT NOT NULL,
    price         DECIMAL(19, 2) NOT NULL,
    total_price   DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (order_id, product_id, size_id),
    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id) REFERENCES tbl_orders (id),
    CONSTRAINT fk_order_item_product
        FOREIGN KEY (product_id) REFERENCES tbl_products (id),
    CONSTRAINT fk_order_item_size
        FOREIGN KEY (size_id) REFERENCES tbl_sizes (id)
);

CREATE INDEX idx_receivers_user_id ON tbl_recivers (user_id);
CREATE INDEX idx_categories_parent_id ON tbl_categories (parent_id);
CREATE INDEX idx_categories_code ON tbl_categories (code);
CREATE INDEX idx_providers_code ON tbl_providers (code);
CREATE INDEX idx_products_code ON tbl_products (code);
CREATE INDEX idx_products_category_id ON tbl_products (category_id);
CREATE INDEX idx_products_provider_id ON tbl_products (provider_id);
CREATE INDEX idx_cart_items_product_id ON tbl_cart_items (product_id);
CREATE INDEX idx_cart_items_size_id ON tbl_cart_items (size_id);
CREATE INDEX idx_promotion_product_promotion_id ON tbl_promotion_product (promotion_id);
CREATE INDEX idx_promotion_category_promotion_id ON tbl_promotion_category (promotion_id);
CREATE INDEX idx_user_vouchers_user_id ON tbl_user_vouchers (user_id);
CREATE INDEX idx_orders_user_id ON tbl_orders (user_id);
CREATE INDEX idx_orders_receiver_id ON tbl_orders (receiver_id);
CREATE INDEX idx_order_items_product_id ON tbl_order_items (product_id);
CREATE INDEX idx_order_items_size_id ON tbl_order_items (size_id);
