-- liquibase formatted sql
-- changeset gcs:202504171200-create-message-entities

-- OPERATOR

create table if not exists `operator_mail_messages` (
    `id` VARCHAR(36) PRIMARY KEY,
    `applicant_id` VARCHAR(36) NOT NULL,
    `applicant_email` VARCHAR(255) NOT NULL,
    `sender_id` VARCHAR(36) NOT NULL,
    `subject` VARCHAR(255) NOT NULL,
    `content` mediumText NOT NULL,
    `attachment_ids` json,
    `sent_at` datetime,
    `scheduled_at` datetime,
    `parent_id` VARCHAR(36) NOT NULL,
    `status_code` VARCHAR(20),
    `error_message` VARCHAR(500),
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    INDEX `operator_mail_messages_applicant_id` (`applicant_id`),
    INDEX `operator_mail_messages_sender_id` (`sender_id`),
    FOREIGN KEY (`parent_id`) references `operator_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;


-- OEM

create table if not exists `oem_mail_messages` (
    `id` VARCHAR(36) PRIMARY KEY,
    `applicant_id` VARCHAR(36) NOT NULL,
    `applicant_email` VARCHAR(255) NOT NULL,
    `sender_id` VARCHAR(36) NOT NULL,
    `subject` VARCHAR(255) NOT NULL,
    `content` mediumText NOT NULL,
    `attachment_ids` json,
    `sent_at` datetime,
    `scheduled_at` datetime,
    `parent_id` VARCHAR(36) NOT NULL,
    `oem_group_id` VARCHAR(36) NOT NULL,
    `status_code` VARCHAR(20),
    `error_message` VARCHAR(500),
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    INDEX `oem_mail_messages_applicant_id` (`applicant_id`),
    INDEX `oem_mail_messages_sender_id` (`sender_id`),
    FOREIGN KEY (`parent_id`) references `oem_client_accounts` (`id`),
    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
