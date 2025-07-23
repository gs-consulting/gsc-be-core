-- liquibase formatted sql
-- changeset gcs:202503101600-create-files-entities

-- Operator

create table if not exists `operator_files` (
    `id` VARCHAR(36) PRIMARY KEY,
    `file_name` VARCHAR(255) NOT NULL,
    `size` bigint NOT NULL,
    `extension` VARCHAR(15) NOT NULL,
    `is_image`  BIT(1) NOT NULL,
    `thumbnail_url` VARCHAR(150),
    `bucket_url` VARCHAR(150) NOT NULL,
    `is_applicant_file` BIT(1) NOT NULL DEFAULT b'0' COMMENT '応募者から届いたファイル',
    `parent_id` VARCHAR(36) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_project_attachments` (
    `project_id` VARCHAR(36) NOT NULL,
    `file_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`project_id`, `file_id`),
    FOREIGN KEY (`project_id`) references `operator_projects` (`id`),
    FOREIGN KEY (`file_id`) references `operator_files` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE `operator_advertisements` ADD `attachment_id` VARCHAR(36) after `linking_type`;
ALTER TABLE `operator_advertisements` ADD CONSTRAINT fk_operator_advertisement_file FOREIGN KEY (attachment_id) REFERENCES `operator_files`(id);

-- OEM

create table if not exists `oem_files` (
    `id` VARCHAR(36) PRIMARY KEY,
    `file_name` VARCHAR(255) NOT NULL,
    `size` bigint NOT NULL,
    `extension` VARCHAR(15) NOT NULL,
    `is_image`  BIT(1) NOT NULL,
    `thumbnail_url` VARCHAR(150),
    `bucket_url` VARCHAR(150) NOT NULL,
    `is_applicant_file` BIT(1) NOT NULL DEFAULT b'0' COMMENT '応募者から届いたファイル',
    `oem_group_id` VARCHAR(36) NOT NULL,
    `parent_id` VARCHAR(36) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`parent_id`) references `oem_client_accounts` (`id`),
    FOREIGN KEY (`oem_group_id`) references `oem_groups` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_project_attachments` (
    `project_id` VARCHAR(36) NOT NULL,
    `file_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`project_id`, `file_id`),
    FOREIGN KEY (`project_id`) references `oem_projects` (`id`),
    FOREIGN KEY (`file_id`) references `oem_files` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE `oem_advertisements` ADD `attachment_id` VARCHAR(36) after `linking_type`;
ALTER TABLE `oem_advertisements` ADD CONSTRAINT fk_oem_advertisement_file FOREIGN KEY (attachment_id) REFERENCES `oem_files`(id);
