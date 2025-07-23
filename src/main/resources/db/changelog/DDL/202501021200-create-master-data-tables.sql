-- liquibase formatted sql
-- changeset gcs:202501091200-create-master-data-tables

create table if not exists `prefectures` (
    `id` INT PRIMARY KEY,
    `name` VARCHAR(255) unique not null
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `cities` (
    `id` INT PRIMARY KEY,
    `name` VARCHAR(255) not null,
    `prefecture_id` INT,

    FOREIGN KEY (`prefecture_id`) references `prefectures` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
