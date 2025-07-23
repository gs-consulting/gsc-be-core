-- liquibase formatted sql
-- changeset gcs:202502030800-create-new-entities

-- Operator

create table if not exists `operator_teams` (
    `id` VARCHAR(36) PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `furigana_name` VARCHAR(255) NOT NULL,
    `team_code` VARCHAR(255),
    `post_code` VARCHAR(7),
    `prefecture_id` INT,
    `city_id` INT,
    `tel` VARCHAR(15),
    `fax_code` VARCHAR(30),
    `email` VARCHAR(255),
    `memo` TEXT,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_account_teams` (
    `operator_id` VARCHAR(36) NOT NULL,
    `team_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`operator_id`, `team_id`),
    FOREIGN KEY (`operator_id`) references `operator_accounts` (`id`),
    FOREIGN KEY (`team_id`) references `operator_teams` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_branches` (
    `id` VARCHAR(36) PRIMARY KEY,
    `branch_name` VARCHAR(255) NOT NULL,
    `furigana_name` VARCHAR(255) NOT NULL,
    `branch_code` VARCHAR(255),
    `post_code` VARCHAR(7),
    `prefecture_id` INT,
    `city_id` INT,
    `tel` VARCHAR(15),
    `fax_code` VARCHAR(30),
    `email` VARCHAR(255),
    `memo` TEXT,
    `staff_permission` VARCHAR(15),
    `part_time_permission` VARCHAR(15),
    `parent_id` VARCHAR(36),
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`parent_id`) references `operator_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_stores` (
    `id` VARCHAR(36) PRIMARY KEY,
    `store_name` VARCHAR(255) NOT NULL,
    `furigana_name` VARCHAR(255) NOT NULL,
    `store_code` VARCHAR(255),
    `post_code` VARCHAR(7),
    `prefecture_id` INT,
    `city_id` INT,
    `tel` VARCHAR(15),
    `fax_code` VARCHAR(30),
    `email` VARCHAR(255),
    `memo` TEXT,
    `parent_id` VARCHAR(36),
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`parent_id`) references `operator_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_branch_stores` (
    `store_id` VARCHAR(36) NOT NULL,
    `branch_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`store_id`, `branch_id`),
    FOREIGN KEY (`store_id`) references `operator_stores` (`id`),
    FOREIGN KEY (`branch_id`) references `operator_branches` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_client_locations` (
    `id` bigint PRIMARY KEY auto_increment,
    `operator_client_id` VARCHAR(36) NOT NULL,
    `branch_id` VARCHAR(36),
    `store_id` VARCHAR(36),

    FOREIGN KEY (`operator_client_id`) references `operator_client_accounts` (`id`),
    FOREIGN KEY (`branch_id`) references `operator_branches` (`id`),
    FOREIGN KEY (`store_id`) references `operator_stores` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- OEM

create table if not exists `oem_teams` (
    `id` VARCHAR(36) PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `furigana_name` VARCHAR(255) NOT NULL,
    `team_code` VARCHAR(255),
    `post_code` VARCHAR(7),
    `prefecture_id` INT,
    `city_id` INT,
    `tel` VARCHAR(15),
    `fax_code` VARCHAR(30),
    `email` VARCHAR(255),
    `memo` TEXT,
    `oem_group_id` VARCHAR(36) NOT NULL,
    `oem_parent_id` VARCHAR(36) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    FOREIGN KEY (`oem_parent_id`) references `oem_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_account_teams` (
    `oem_id` VARCHAR(36) NOT NULL,
    `team_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`oem_id`, `team_id`),
    FOREIGN KEY (`oem_id`) references `oem_accounts` (`id`),
    FOREIGN KEY (`team_id`) references `oem_teams` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_branches` (
    `id` VARCHAR(36) PRIMARY KEY,
    `branch_name` VARCHAR(255) NOT NULL,
    `furigana_name` VARCHAR(255) NOT NULL,
    `branch_code` VARCHAR(255),
    `post_code` VARCHAR(7),
    `prefecture_id` INT,
    `city_id` INT,
    `tel` VARCHAR(15),
    `fax_code` VARCHAR(30),
    `email` VARCHAR(255),
    `memo` TEXT,
    `staff_permission` VARCHAR(15),
    `part_time_permission` VARCHAR(15),
    `oem_group_id` VARCHAR(36) NOT NULL,
    `oem_parent_id` VARCHAR(36) NOT NULL,
    `parent_id` VARCHAR(36),
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    FOREIGN KEY (`oem_parent_id`) references `oem_accounts` (`id`),
    FOREIGN KEY (`parent_id`) references `oem_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_stores` (
    `id` VARCHAR(36) PRIMARY KEY,
    `store_name` VARCHAR(255) NOT NULL,
    `furigana_name` VARCHAR(255) NOT NULL,
    `store_code` VARCHAR(255),
    `post_code` VARCHAR(7),
    `prefecture_id` INT,
    `city_id` INT,
    `tel` VARCHAR(15),
    `fax_code` VARCHAR(30),
    `email` VARCHAR(255),
    `memo` TEXT,
    `oem_group_id` VARCHAR(36) NOT NULL,
    `oem_parent_id` VARCHAR(36) NOT NULL,
    `parent_id` VARCHAR(36),
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    FOREIGN KEY (`oem_parent_id`) references `oem_accounts` (`id`),
    FOREIGN KEY (`parent_id`) references `oem_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_branch_stores` (
    `store_id` VARCHAR(36) NOT NULL,
    `branch_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`store_id`, `branch_id`),
    FOREIGN KEY (`store_id`) references `oem_stores` (`id`),
    FOREIGN KEY (`branch_id`) references `oem_branches` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_client_locations` (
    `id` bigint PRIMARY KEY auto_increment,
    `oem_client_id` VARCHAR(36) NOT NULL,
    `branch_id` VARCHAR(36),
    `store_id` VARCHAR(36),

    FOREIGN KEY (`oem_client_id`) references `oem_client_accounts` (`id`),
    FOREIGN KEY (`branch_id`) references `oem_branches` (`id`),
    FOREIGN KEY (`store_id`) references `oem_stores` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
