ALTER TABLE tbl_vouchers
    ADD COLUMN IF NOT EXISTS apply_scope VARCHAR(20) NOT NULL DEFAULT 'NEWBIE';

UPDATE tbl_vouchers
SET apply_scope = 'NEWBIE'
WHERE apply_scope IS NULL;
