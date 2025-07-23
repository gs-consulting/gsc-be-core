-- liquibase formatted sql
-- changeset gcs:202501030800-create-new-entities

create table if not exists `notifications` (
    `id` VARCHAR(36) PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `posting_start_date` DATE NOT NULL,
    `posting_end_date` DATE NOT NULL,
    `content` TEXT,
    `status` VARCHAR(15) NOT NULL DEFAULT 'PRIVATE',
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    INDEX idx_notifications(posting_start_date, posting_end_date)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `selection_statuses` (
    `id` bigint PRIMARY KEY auto_increment,
    `item_name` VARCHAR(255) NOT NULL,
    `flow_type` INT NOT NULL,
    `display_order` INT NOT NULL,
    `oem_group_id` VARCHAR(36),
    `parent_id` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    INDEX `selection_statuses_idx_parent_id` (`parent_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX idx_selection_statuses ON selection_statuses (flow_type, display_order, parent_id, oem_group_id);

create table if not exists `media_report_displays` (
    `id` VARCHAR(36) PRIMARY KEY,
    `flow_type_id` INT NOT NULL,
    `is_enabled` BIT(1) NOT NULL DEFAULT b'0',
    `oem_group_id` VARCHAR(36),
    `parent_id` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    INDEX `media_report_displays_idx_parent_id` (`parent_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX idx_media_report_displays ON media_report_displays (flow_type_id, is_enabled, parent_id, oem_group_id);

create table if not exists `master_statuses` (
    `id` bigint PRIMARY KEY auto_increment,
    `status_name` VARCHAR(255) NOT NULL,
    `order` INT NOT NULL,
    `oem_group_id` VARCHAR(36),
    `parent_id` VARCHAR(36) NOT NULL,
    `type` VARCHAR(15) NOT NULL,

    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    INDEX `master_statuses_idx_parent_id` (`parent_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX idx_master_statuses ON master_statuses (`order`, type, parent_id, oem_group_id);

create table if not exists `interview_categories` (
    `id` bigint PRIMARY KEY auto_increment,
    `category_name` VARCHAR(255) NOT NULL,
    `order` INT NOT NULL,
    `oem_group_id` VARCHAR(36),
    `parent_id` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`),
    INDEX `interview_categories_idx_parent_id` (`parent_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX idx_interview_categories ON interview_categories (`order`, parent_id, oem_group_id);
