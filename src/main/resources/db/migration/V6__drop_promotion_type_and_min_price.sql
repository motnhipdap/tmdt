ALTER TABLE tbl_promotions
    DROP COLUMN IF EXISTS type,
    DROP COLUMN IF EXISTS min_price_apply;
