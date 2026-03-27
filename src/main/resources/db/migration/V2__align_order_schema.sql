ALTER TABLE tbl_orders
    RENAME COLUMN total_price TO total_amount;

ALTER TABLE tbl_orders
    ADD COLUMN code VARCHAR(20);

ALTER TABLE tbl_orders
    ADD COLUMN note VARCHAR(500);

ALTER TABLE tbl_orders
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

ALTER TABLE tbl_orders
    ADD COLUMN receiver_id INT;

UPDATE tbl_orders
SET code = 'ORD-' || LPAD(id::text, 8, '0')
WHERE code IS NULL;

ALTER TABLE tbl_orders
    ALTER COLUMN code SET NOT NULL;

ALTER TABLE tbl_orders
    ADD CONSTRAINT uk_orders_code UNIQUE (code);

CREATE TABLE tbl_order_items
(
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    size_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (order_id, product_id, size_id),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES tbl_orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES tbl_products (id),
    CONSTRAINT fk_order_item_size FOREIGN KEY (size_id) REFERENCES tbl_size (id)
);

CREATE INDEX idx_order_item_product ON tbl_order_items (product_id);
CREATE INDEX idx_order_item_size ON tbl_order_items (size_id);
