ALTER TABLE tbl_user_vouchers
    ADD COLUMN IF NOT EXISTS end_at TIMESTAMP(3);

ALTER TABLE tbl_user_vouchers
    ADD COLUMN IF NOT EXISTS min_price_apply DECIMAL(19, 2);

UPDATE tbl_user_vouchers uv
SET end_at = v.end_at,
    min_price_apply = COALESCE(uv.min_price_apply, v.min_order_amount)
FROM tbl_vouchers v
WHERE uv.voucher_id = v.id
  AND (uv.end_at IS NULL OR uv.min_price_apply IS NULL);
