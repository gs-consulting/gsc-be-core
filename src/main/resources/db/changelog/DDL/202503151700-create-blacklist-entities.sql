-- liquibase formatted sql
-- changeset gcs:202503151700-create-blacklist-entities

create table if not exists `blacklists` (
    `id` VARCHAR(36) PRIMARY KEY,
    `parent_id` VARCHAR(36) NOT NULL,
    `oem_group_id` VARCHAR(36),
    `full_name` VARCHAR(255) NOT NULL,
    `birthday` DATE,
    `tel` VARCHAR(15),
    `email` VARCHAR(255),
    `memo` TEXT,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;