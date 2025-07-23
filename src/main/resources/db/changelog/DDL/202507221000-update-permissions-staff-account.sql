-- liquibase formatted sql
-- changeset gcs:202507221000-update-permissions-staff-account

ALTER TABLE `operator_accounts` ADD COLUMN `permissions` JSON NULL COMMENT '機能制限' AFTER `full_name`;

ALTER TABLE `oem_accounts` ADD COLUMN `permissions` JSON NULL COMMENT '機能制限' AFTER `memo`;
