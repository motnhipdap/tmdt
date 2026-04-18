-- Gộp cột address vào tbl_recivers, bỏ bảng tbl_address

ALTER TABLE tbl_recivers
    ADD COLUMN IF NOT EXISTS country  VARCHAR(20),
    ADD COLUMN IF NOT EXISTS province VARCHAR(20),
    ADD COLUMN IF NOT EXISTS district VARCHAR(20),
    ADD COLUMN IF NOT EXISTS street   VARCHAR(100),
    ADD COLUMN IF NOT EXISTS detail   VARCHAR(255);

UPDATE tbl_recivers r
SET country  = a.country,
    province = a.province,
    district = a.district,
    street   = a.street,
    detail   = a.detail
FROM tbl_address a
WHERE a.receiver_id = r.id;

DROP TABLE IF EXISTS tbl_address;
