-- liquibase formatted sql
-- changeset gcs:202503171500-create-template-entities

create table if not exists `operator_templates` (
    `id` VARCHAR(36) PRIMARY KEY,
    `parent_id` VARCHAR(36) NOT NULL,
    `template_name` VARCHAR(255) NOT NULL,
    `content` TEXT NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`parent_id`) references `operator_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_templates` (
    `id` VARCHAR(36) PRIMARY KEY,
    `parent_id` VARCHAR(36) NOT NULL,
    `oem_group_id` VARCHAR(36) NOT NULL,
    `template_name` VARCHAR(255) NOT NULL,
    `content` TEXT NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`parent_id`) references `oem_client_accounts` (`id`),
    INDEX `oem_templates_client_id` (`oem_group_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
