-- liquibase formatted sql
-- changeset gcs:202507070800-update-phone-number-column

ALTER TABLE `operator_applicants` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `oem_applicants` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `blacklists` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `operator_client_accounts` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `oem_client_accounts` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `operator_branches` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `operator_stores` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `oem_branches` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `oem_stores` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `operator_teams` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `oem_teams` MODIFY COLUMN `tel` VARCHAR(30);

ALTER TABLE `oem_accounts` MODIFY COLUMN `tel` VARCHAR(30);
