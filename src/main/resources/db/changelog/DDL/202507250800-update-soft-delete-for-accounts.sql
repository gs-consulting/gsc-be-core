-- liquibase formatted sql
-- changeset gcs:202507250800-update-soft-delete-for-accounts

ALTER TABLE `oem_accounts` ADD COLUMN `is_deleted` BIT(1) NOT NULL DEFAULT b'0' AFTER `permissions`;

ALTER TABLE `operator_accounts` ADD COLUMN `is_deleted` BIT(1) NOT NULL DEFAULT b'0' AFTER `permissions`;

ALTER TABLE `oem_client_accounts` ADD COLUMN `is_deleted` BIT(1) NOT NULL DEFAULT b'0' AFTER `is_domain_enabled`;

ALTER TABLE `operator_client_accounts` ADD COLUMN `is_deleted` BIT(1) NOT NULL DEFAULT b'0' AFTER `is_domain_enabled`;

ALTER TABLE `oem_applicants` ADD COLUMN `is_deleted` BIT(1) NOT NULL DEFAULT b'0' AFTER `is_unread`;

ALTER TABLE `operator_applicants` ADD COLUMN `is_deleted` BIT(1) NOT NULL DEFAULT b'0' AFTER `is_unread`;
