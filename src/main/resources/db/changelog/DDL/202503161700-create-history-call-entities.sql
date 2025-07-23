-- liquibase formatted sql
-- changeset gcs:202503161700-create-history-call-entities

create table if not exists `operator_history_calls` (
    `id` VARCHAR(36) PRIMARY KEY,
    `applicant_id` VARCHAR(36) NOT NULL,
    `pic_id` VARCHAR(36) NOT NULL,
    `call_start_date` datetime NOT NULL,
    `call_end_date` datetime NOT NULL,
    `memo` TEXT,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`applicant_id`) references `operator_applicants` (`id`),
    FOREIGN KEY (`pic_id`) references `operator_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_history_calls` (
    `id` VARCHAR(36) PRIMARY KEY,
    `applicant_id` VARCHAR(36) NOT NULL,
    `pic_id` VARCHAR(36) NOT NULL,
    `call_start_date` datetime NOT NULL,
    `call_end_date` datetime NOT NULL,
    `memo` TEXT,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`applicant_id`) references `oem_applicants` (`id`),
    FOREIGN KEY (`pic_id`) references `oem_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
