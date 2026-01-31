ALTER TABLE db1.tbl_users
    ADD catalog_id BIGINT NULL;

ALTER TABLE db1.tbl_users
    ADD updated_at datetime NULL;

ALTER TABLE db1.tbl_users
    ADD CONSTRAINT uc_tbl_users_acc UNIQUE (acc_id);

DROP TABLE db1.tbl_otp_verification;

ALTER TABLE db1.tbl_users
    DROP COLUMN update_at;

ALTER TABLE db1.tbl_users
    DROP COLUMN id;

ALTER TABLE db1.tbl_users
    ADD id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY;

ALTER TABLE db1.tbl_users
    ADD CONSTRAINT uc_tbl_users_acc UNIQUE (id);

ALTER TABLE db1.tbl_users
    MODIFY name VARCHAR(255);

ALTER TABLE db1.tbl_users
    MODIFY name VARCHAR(255) NULL;