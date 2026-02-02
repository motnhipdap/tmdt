-- =========================
-- DATABASE CONFIG
-- =========================
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- 4. POINT
-- =========================
CREATE TABLE tbl_point
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    current     INT DEFAULT 0,
    time_expire TIMESTAMP,
    user_id     CHAR(36),
    CONSTRAINT fk_point_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id)
            ON DELETE CASCADE
) ENGINE = InnoDB;

-- =========================
-- 5. POINT HISTORY
-- =========================
CREATE TABLE tbl_point_history
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    CHAR(36),
    action     ENUM ('EARN','USE','REFUND','EXPIRE') NOT NULL,
    point      INT                                   NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    invoice_id INT,
    CONSTRAINT fk_point_history_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id)
            ON DELETE CASCADE
) ENGINE = InnoDB;

-- =========================
-- 6. CATEGORIES
-- =========================
CREATE TABLE tbl_categories
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      NVARCHAR(50) NOT NULL,
    img_url   VARCHAR(255),
    `desc`    NVARCHAR(50),
    status    VARCHAR(20) DEFAULT 'active',
    parent_id INT,
    create_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_category_parent
        FOREIGN KEY (parent_id) REFERENCES tbl_categories (id)
            ON DELETE SET NULL
) ENGINE = InnoDB;

-- =========================
-- 8. PROVIDERS
-- =========================
CREATE TABLE tbl_providers
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      NVARCHAR(50) NOT NULL,
    `desc`    NVARCHAR(255),
    email     VARCHAR(100),
    phone     VARCHAR(15),
    status    VARCHAR(20) DEFAULT 'ACTIVE',
    create_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- =========================
-- 7. PRODUCTS
-- =========================
CREATE TABLE tbl_products
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    name           NVARCHAR(50) NOT NULL,
    `desc`         NVARCHAR(255),
    quantity       INT         DEFAULT 0,
    quantity_slold INT         DEFAULT 0,
    price          INT,
    status         VARCHAR(20) DEFAULT 'active',
    rated          FLOAT,
    category_id    INT,
    provider_id    INT,
    create_at      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    update_at      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES tbl_categories (id),
    CONSTRAINT fk_product_provider
        FOREIGN KEY (provider_id) REFERENCES tbl_providers (id)
) ENGINE = InnoDB;

-- =========================
-- 9. COMMENTS
-- =========================
CREATE TABLE tbl_comments
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    `desc`     NVARCHAR(255),
    rate       TINYINT,
    user_id    CHAR(36),
    product_id INT,
    create_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_comment_product
        FOREIGN KEY (product_id) REFERENCES tbl_products (id)
            ON DELETE CASCADE
) ENGINE = InnoDB;

-- =========================
-- 10. CARTS
-- =========================
CREATE TABLE tbl_carts
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id)
            ON DELETE CASCADE
) ENGINE = InnoDB;

-- =========================
-- 11. ITEM CARTS
-- =========================
CREATE TABLE tbl_item_carts
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    cart_id    INT,
    product_id INT,
    quantity   INT,
    CONSTRAINT fk_item_cart
        FOREIGN KEY (cart_id) REFERENCES tbl_carts (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_item_product
        FOREIGN KEY (product_id) REFERENCES tbl_products (id)
) ENGINE = InnoDB;

-- =========================
-- 12. VOUCHERS
-- =========================
CREATE TABLE tbl_vouchers
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    code         VARCHAR(30) UNIQUE,
    type         VARCHAR(20),
    percent      INT,
    value        INT,
    min_price    INT,
    max_discount INT,
    url_img      VARCHAR(255)
) ENGINE = InnoDB;

-- =========================
-- 13. USER VOUCHER
-- =========================
CREATE TABLE tbl_user_voucher
(
    user_id    CHAR(36),
    voucher_id INT,
    status     VARCHAR(20) DEFAULT 'active',
    quantity   INT         DEFAULT 1,
    start_at   TIMESTAMP,
    end_at     TIMESTAMP,
    PRIMARY KEY (user_id, voucher_id),
    CONSTRAINT fk_user_voucher_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_user_voucher_voucher
        FOREIGN KEY (voucher_id) REFERENCES tbl_vouchers (id)
            ON DELETE CASCADE
) ENGINE = InnoDB;

-- =========================
-- 14. INVOICES
-- =========================
CREATE TABLE tbl_invoices
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    code           VARCHAR(30) UNIQUE,
    type           VARCHAR(10),
    status         VARCHAR(20),
    total_money    INT,
    total_discount INT,
    create_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id        CHAR(36),
    CONSTRAINT fk_invoice_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id)
) ENGINE = InnoDB;

-- =========================
-- 15. INVOICE DETAIL
-- =========================
CREATE TABLE invoice_detail
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT,
    product_id INT,
    quantity   INT,
    voucher_id INT,
    discount   INT,
    total      INT,
    CONSTRAINT fk_detail_invoice
        FOREIGN KEY (invoice_id) REFERENCES tbl_invoices (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_detail_product
        FOREIGN KEY (product_id) REFERENCES tbl_products (id),
    CONSTRAINT fk_detail_voucher
        FOREIGN KEY (voucher_id) REFERENCES tbl_vouchers (id)
) ENGINE = InnoDB;

-- =========================
-- 16. PRODUCT IMAGES
-- =========================
CREATE TABLE tbl_product_img
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    image_url  VARCHAR(255) NOT NULL,
    is_main    BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_img
        FOREIGN KEY (product_id) REFERENCES tbl_products (id)
            ON DELETE CASCADE
) ENGINE = InnoDB;

-- =========================
-- 17. USER AVATAR
-- =========================
CREATE TABLE tbl_avata_img
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    CHAR(36),
    image_url  VARCHAR(255) NOT NULL,
    is_main    BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_avatar_user
        FOREIGN KEY (user_id) REFERENCES tbl_users (id)
            ON DELETE CASCADE
) ENGINE = InnoDB;

-- =========================
-- 18. PROVIDER LOGO
-- =========================
CREATE TABLE tbl_provider_logo
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    provider_id INT,
    image_url   VARCHAR(255) NOT NULL,
    is_main     BOOLEAN   DEFAULT FALSE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_provider_logo
        FOREIGN KEY (provider_id) REFERENCES tbl_providers (id)
            ON DELETE CASCADE
) ENGINE = InnoDB;

SET FOREIGN_KEY_CHECKS = 1;
