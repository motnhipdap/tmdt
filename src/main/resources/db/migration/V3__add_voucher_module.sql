CREATE TABLE tbl_vouchers
(
    id               SERIAL PRIMARY KEY,
    code             VARCHAR(30)    NOT NULL UNIQUE,
    type             VARCHAR(20)    NOT NULL,
    value            INT            NOT NULL,
    min_order_amount DECIMAL(19, 2) NOT NULL DEFAULT 0,
    start_at         TIMESTAMP(3),
    end_at           TIMESTAMP(3),
    status           VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
    version          BIGINT         NOT NULL DEFAULT 0,
    created_at       TIMESTAMP(3)            DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP(3)            DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_user_vouchers
(
    id              SERIAL PRIMARY KEY,
    user_id         CHAR(36)     NOT NULL,
    voucher_id      INT          NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE',
    used_at         TIMESTAMP(3),
    used_order_code VARCHAR(20),
    version         BIGINT       NOT NULL DEFAULT 0,
    created_at      TIMESTAMP(3)          DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP(3)          DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_voucher_user FOREIGN KEY (user_id) REFERENCES tbl_users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_voucher_voucher FOREIGN KEY (voucher_id) REFERENCES tbl_vouchers (id) ON DELETE CASCADE,
    CONSTRAINT uk_user_voucher UNIQUE (user_id, voucher_id)
);

CREATE INDEX idx_user_voucher_user ON tbl_user_vouchers (user_id);
CREATE INDEX idx_user_voucher_status ON tbl_user_vouchers (status);

ALTER TABLE tbl_orders
    ADD COLUMN subtotal_amount DECIMAL(19, 2) NOT NULL DEFAULT 0;

ALTER TABLE tbl_orders
    ADD COLUMN voucher_discount_amount DECIMAL(19, 2) NOT NULL DEFAULT 0;

ALTER TABLE tbl_orders
    ADD COLUMN voucher_code VARCHAR(30);

UPDATE tbl_orders
SET subtotal_amount = total_amount
WHERE subtotal_amount = 0;
