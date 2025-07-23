-- liquibase formatted sql
-- changeset gcs:202504091700-create-applicant-count-view

CREATE OR REPLACE VIEW `oem_applicants_count_view` AS
SELECT
    oa.id,
    oa.oem_group_id,
    oa.parent_id,
    s.flow_type as selection_status_type,
    oa.project_id,
    oal.advertisement_id,
    oa.created_at
FROM oem_applicants oa
         LEFT JOIN selection_statuses s ON oa.selection_status_id = s.id
         LEFT JOIN oem_advertisement_linkings oal ON oa.project_id = oal.project_id
WHERE oa.is_mail_duplicate = false AND oa.is_tel_duplicate = false AND oa.blacklist1_id IS NULL AND oa.blacklist2_id IS NULL
ORDER BY oa.created_at DESC;

CREATE OR REPLACE VIEW `operator_applicants_count_view` AS
SELECT
    oa.id,
    oa.parent_id,
    s.flow_type as selection_status_type,
    oa.project_id,
    oal.advertisement_id,
    oa.created_at
FROM operator_applicants oa
         LEFT JOIN selection_statuses s ON oa.selection_status_id = s.id
         LEFT JOIN operator_advertisement_linkings oal ON oa.project_id = oal.project_id
WHERE oa.is_mail_duplicate = false AND oa.is_tel_duplicate = false AND oa.blacklist1_id IS NULL AND oa.blacklist2_id IS NULL
ORDER BY oa.created_at DESC;
