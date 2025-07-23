-- liquibase formatted sql
-- changeset gcs:202507210900-modify-branch-store-tables

ALTER TABLE `operator_stores` ADD COLUMN `branch_id` VARCHAR(36) COMMENT '支店' AFTER `furigana_name`;
ALTER TABLE `oem_stores` ADD COLUMN `branch_id` VARCHAR(36) COMMENT '支店' AFTER `furigana_name`;

DROP TABLE `operator_branch_stores`;
DROP TABLE `oem_branch_stores`;
