-- liquibase formatted sql
-- changeset gcs:202502101400-create-media-entities

create table if not exists `master_medias` (
    `id` VARCHAR(36) PRIMARY KEY,
    `media_name` VARCHAR(255) NOT NULL,
    `media_code` VARCHAR(255),
    `amount` INT not null,
    `hex_color` VARCHAR(15) NOT NULL DEFAULT '#00000' COMMENT '色',
    `memo` TEXT,
    `site_name` VARCHAR(20),
    `login_id` VARCHAR(255),
    `password` VARCHAR(255),
    `parent_id` VARCHAR(36) NOT NULL,
    `oem_group_id` VARCHAR(36),
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    INDEX `master_medias_idx_media_name` (`media_name`),
    INDEX `master_medias_idx_parent_id` (`parent_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `monthly_costs` (
    `id` VARCHAR(36) PRIMARY KEY,
    `media_id` VARCHAR(36) not null,
    `start_month` DATE not null,
    `amount` INT not null,
    `oem_group_id` VARCHAR(36),
    `parent_id` VARCHAR(36) not null,

    FOREIGN KEY (`media_id`) references `master_medias` (`id`),
    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    INDEX `month_costs_idx_parent_id` (`parent_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
