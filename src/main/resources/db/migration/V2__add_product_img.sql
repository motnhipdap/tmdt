-- Flyway Migration V2: Fix missing columns

-- tbl_providers: entity Provider has @Version but column was missing
ALTER TABLE tbl_providers
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;


