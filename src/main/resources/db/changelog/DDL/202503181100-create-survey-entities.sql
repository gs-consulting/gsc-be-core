-- liquibase formatted sql
-- changeset gcs:202503181100-create-survey-entities

-- OPERATOR

create table if not exists `operator_surveys` (
    `id` VARCHAR(36) PRIMARY KEY,
    `survey_name` VARCHAR(255) NOT NULL,
    `parent_id` VARCHAR(36) NOT NULL,
    `is_deletable` BIT(1) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`parent_id`) references `operator_client_accounts` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_survey_questions` (
    `id` bigint PRIMARY KEY auto_increment,
    `question_text` TEXT NOT NULL,
    `survey_id` VARCHAR(36) NOT NULL,
    `type` VARCHAR(15) NOT NULL,
    `is_required` BIT(1) NOT NULL,

    FOREIGN KEY (`survey_id`) references `operator_surveys` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_survey_answers` (
    `id` bigint PRIMARY KEY auto_increment,
    `answer_text` TEXT NOT NULL,
    `question_id` bigint NOT NULL,
    `is_fixed` BIT(1) NOT NULL,

    FOREIGN KEY (`question_id`) references `operator_survey_questions` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_applicant_surveys` (
    `id` VARCHAR(36) PRIMARY KEY,
    `url` VARCHAR(255) NOT NULL,
    `applicant_id` VARCHAR(36) NOT NULL,
    `survey_id` VARCHAR(36) NOT NULL,
    `sent_at` datetime NOT NULL,
    `replied_at` datetime,

    FOREIGN KEY (`applicant_id`) references `operator_applicants` (`id`),
    FOREIGN KEY (`survey_id`) references `operator_surveys` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `operator_applicant_answers` (
    `id` bigint PRIMARY KEY auto_increment,
    `applicant_survey_id` VARCHAR(36) NOT NULL,
    `question_id` bigint NOT NULL,
    `selected_answer_id` bigint,
    `answer_text` TEXT,
    `created_at` timestamp DEFAULT current_timestamp,
    
    FOREIGN KEY (`applicant_survey_id`) references `operator_applicant_surveys` (`id`),
    FOREIGN KEY (`question_id`) references `operator_survey_questions` (`id`),
    FOREIGN KEY (`selected_answer_id`) references `operator_survey_answers` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- OEM

create table if not exists `oem_surveys` (
    `id` VARCHAR(36) PRIMARY KEY,
    `survey_name` VARCHAR(255) NOT NULL,
    `parent_id` VARCHAR(36) NOT NULL,
    `oem_group_id` VARCHAR(36) NOT NULL,
    `is_deletable` BIT(1) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp ON update current_timestamp,
    `created_by` VARCHAR(36) NOT NULL,
    `updated_by` VARCHAR(36) NOT NULL,

    FOREIGN KEY (`parent_id`) references `oem_client_accounts` (`id`),
    INDEX `oem_surveys_client_id_idx` (`oem_group_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_survey_questions` (
    `id` bigint PRIMARY KEY auto_increment,
    `question_text` TEXT NOT NULL,
    `survey_id` VARCHAR(36) NOT NULL,
    `type` VARCHAR(15) NOT NULL,
    `is_required` BIT(1) NOT NULL,

    FOREIGN KEY (`survey_id`) references `oem_surveys` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_survey_answers` (
    `id` bigint PRIMARY KEY auto_increment,
    `answer_text` TEXT NOT NULL,
    `question_id` bigint NOT NULL,
    `is_fixed` BIT(1) NOT NULL,

    FOREIGN KEY (`question_id`) references `oem_survey_questions` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_applicant_surveys` (
    `id` VARCHAR(36) PRIMARY KEY,
    `url` VARCHAR(255) NOT NULL,
    `applicant_id` VARCHAR(36) NOT NULL,
    `survey_id` VARCHAR(36) NOT NULL,
    `sent_at` datetime NOT NULL,
    `replied_at` datetime,

    FOREIGN KEY (`applicant_id`) references `oem_applicants` (`id`),
    FOREIGN KEY (`survey_id`) references `oem_surveys` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `oem_applicant_answers` (
    `id` bigint PRIMARY KEY auto_increment,
    `applicant_survey_id` VARCHAR(36) NOT NULL,
    `question_id` bigint NOT NULL,
    `selected_answer_id` bigint,
    `answer_text` TEXT,
    `created_at` timestamp DEFAULT current_timestamp,

    FOREIGN KEY (`applicant_survey_id`) references `oem_applicant_surveys` (`id`),
    FOREIGN KEY (`question_id`) references `oem_survey_questions` (`id`),
    FOREIGN KEY (`selected_answer_id`) references `oem_survey_answers` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
