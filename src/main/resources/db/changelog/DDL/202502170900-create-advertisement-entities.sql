-- liquibase formatted sql
-- changeset gcs:202502170900-create-advertisement-entities

-- Operator

create table if not exists `operator_advertisements` (
    `id` VARCHAR(36) PRIMARY KEY,
    `master_media_id` VARCHAR(36) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `type` VARCHAR(15) NOT NULL,
    `amount` INT,
    `start_date` DATE NOT NULL,
    `end_date` DATE,
    `memo` TEXT,
    `linking_type` VARCHAR(15) NOT NULL DEFAULT 0,
    `parent_id` VARCHAR(36) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`master_media_id`) references `master_medias` (`id`),
    FOREIGN KEY (`parent_id`) references `operator_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_advertisement_linkings` (
    `advertisement_id` VARCHAR(36) NOT NULL,
    `project_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`advertisement_id`, `project_id`),
    FOREIGN KEY (`advertisement_id`) references `operator_advertisements` (`id`),
    FOREIGN KEY (`project_id`) references `operator_projects` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- OEM

create table if not exists `oem_advertisements` (
    `id` VARCHAR(36) PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `master_media_id` VARCHAR(36) NOT NULL,
    `type` VARCHAR(15) NOT NULL,
    `amount` INT,
    `start_date` DATE NOT NULL,
    `end_date` DATE,
    `memo` TEXT,
    `linking_type` VARCHAR(15) NOT NULL DEFAULT 0,
    `oem_group_id` VARCHAR(36) NOT NULL,
    `parent_id` VARCHAR(36) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`master_media_id`) references `master_medias` (`id`),
    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    FOREIGN KEY (`parent_id`) references `oem_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_advertisement_linkings` (
    `advertisement_id` VARCHAR(36) NOT NULL,
    `project_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`advertisement_id`, `project_id`),
    FOREIGN KEY (`advertisement_id`) references `oem_advertisements` (`id`),
    FOREIGN KEY (`project_id`) references `oem_projects` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
