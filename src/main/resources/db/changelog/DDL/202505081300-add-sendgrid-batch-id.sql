-- liquibase formatted sql
-- changeset gcs:202505081300-add-sendgrid-batch-id

-- Operator
ALTER TABLE `operator_mail_messages` ADD COLUMN `sendgrid_batch_id` VARCHAR(255) COMMENT 'SendgridのBatchID 送信予約キャンセル時に使用' AFTER `scheduled_at`;
ALTER TABLE `operator_mail_messages` ADD INDEX `idx_sendgrid_batch_id` (`sendgrid_batch_id`);
ALTER TABLE `operator_mail_messages` ADD INDEX `idx_mail_message_sent_at` (`sent_at`);

-- OEM
ALTER TABLE `oem_mail_messages` ADD COLUMN `sendgrid_batch_id` VARCHAR(255) COMMENT 'SendgridのBatchID 送信予約キャンセル時に使用' AFTER `scheduled_at`;
ALTER TABLE `oem_mail_messages` ADD INDEX `idx_sendgrid_batch_id` (`sendgrid_batch_id`);
ALTER TABLE `oem_mail_messages` ADD INDEX `idx_mail_message_sent_at` (`sent_at`);
