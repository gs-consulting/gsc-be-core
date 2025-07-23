-- liquibase formatted sql
-- changeset gcs:202501030600-create-accounts

create table if not exists `accounts` (
    `id` VARCHAR(36) PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `reset_token_string` VARCHAR(255),
    `forgot_token_string` VARCHAR(255),
    `enabled` BIT(1) NOT NULL DEFAULT b'0',
    `is_deleted` BIT(1) NOT NULL DEFAULT b'0',
    `is_normal_login` BIT(1) NOT NULL DEFAULT b'1',
    `role` VARCHAR(15) NOT NULL DEFAULT 'CLIENT',
    `sub_role` VARCHAR(15),
    `token_expiration_date` datetime,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    INDEX `accounts_idx_email_role` (`email`, `role`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_groups` (
    `id` VARCHAR(36) PRIMARY KEY,
    `oem_group_name` VARCHAR(255) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_accounts` (
    `id` VARCHAR(36) PRIMARY KEY,
    `full_name` VARCHAR(255) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`id`) references `accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_client_accounts` (
    `id` VARCHAR(36) PRIMARY KEY,
    `client_name` VARCHAR(255) NOT NULL,
    `furigana_name` VARCHAR(255) NOT NULL,
    `client_code` VARCHAR(255),
    `post_code` VARCHAR(7),
    `prefecture_id` INT,
    `city_id` INT,
    `tel` VARCHAR(15),
    `fax_code` VARCHAR(30),
    `memo` TEXT,
    `employment_type` VARCHAR(15),
    `is_interviewer` BIT(1) NOT NULL DEFAULT b'0',
    `is_ini_education_staff` BIT(1) NOT NULL DEFAULT b'0',
    `permissions` json DEFAULT NULL COMMENT '機能制限',
    `domain_setting` VARCHAR(255),
    `is_domain_enabled` BIT(1) NOT NULL DEFAULT b'0',
    `parent_id` VARCHAR(36),
    `oem_group_id` VARCHAR(36) DEFAULT NULL COMMENT 'OEMグループ',
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`id`) references `accounts` (`id`),
    FOREIGN KEY (`parent_id`) references `operator_client_accounts` (`id`),
    INDEX `idx_oem_group_id` (`oem_group_id`),
    INDEX `idx_domain_setting` (`domain_setting`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_client_managers` (
    `operator_client_id` VARCHAR(36) NOT NULL,
    `operator_client_manager_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`operator_client_id`, `operator_client_manager_id`),
    FOREIGN KEY (`operator_client_id`) references `operator_client_accounts` (`id`),
    FOREIGN KEY (`operator_client_manager_id`) references `operator_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_accounts` (
    `id` VARCHAR(36) PRIMARY KEY,
    `full_name` VARCHAR(255) NOT NULL,
    `furigana_name` VARCHAR(255) NOT NULL,
    `tel` VARCHAR(15),
    `fax_code` VARCHAR(30),
    `memo` TEXT,
    `parent_id` VARCHAR(36),
    `oem_group_id` VARCHAR(36) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`id`) references `accounts` (`id`),
    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    FOREIGN KEY (`parent_id`) references `oem_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_client_accounts` (
    `id` VARCHAR(36) PRIMARY KEY,
    `client_name` VARCHAR(255) NOT NULL,
    `furigana_name` VARCHAR(255) NOT NULL,
    `client_code` VARCHAR(255),
    `post_code` VARCHAR(7),
    `prefecture_id` INT,
    `city_id` INT,
    `tel` VARCHAR(15),
    `fax_code` VARCHAR(30),
    `memo` TEXT,
    `employment_type` VARCHAR(15) COMMENT '雇用形態',
    `is_interviewer` BIT(1) NOT NULL DEFAULT b'0' COMMENT '面接官',
    `is_ini_education_staff` BIT(1) NOT NULL DEFAULT b'0' COMMENT '初動教育担当',
    `permissions` json DEFAULT NULL COMMENT '機能制限',
    `domain_setting` VARCHAR(255),
    `is_domain_enabled` BIT(1) NOT NULL DEFAULT b'0',
    `oem_group_id` VARCHAR(36) NOT NULL,
    `oem_account_id` VARCHAR(36) NOT NULL,
    `parent_id` VARCHAR(36),
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`id`) references `accounts` (`id`),
    FOREIGN KEY (`oem_account_id`) references `oem_accounts` (`id`),
    FOREIGN KEY (`parent_id`) references `oem_client_accounts` (`id`),
    INDEX `idx_domain_setting` (`domain_setting`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_client_managers` (
    `oem_client_id` VARCHAR(36) NOT NULL,
    `oem_client_manager_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`oem_client_id`, `oem_client_manager_id`),
    FOREIGN KEY (`oem_client_id`) references `oem_client_accounts` (`id`),
    FOREIGN KEY (`oem_client_manager_id`) references `oem_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
