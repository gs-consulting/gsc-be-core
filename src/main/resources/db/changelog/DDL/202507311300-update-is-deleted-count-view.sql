-- liquibase formatted sql
-- changeset gcs:202507311300-update-is-deleted-count-view

CREATE OR REPLACE VIEW `oem_applicants_count_view` AS
SELECT
    oa.id,
    oa.oem_group_id,
    oa.parent_id,
    s.flow_type as selection_status_type,
    oa.project_id,
    oal.advertisement_id,
    oa.created_at,
    oa.lst_status_change_date_time
FROM oem_applicants oa
         LEFT JOIN selection_statuses s ON oa.selection_status_id = s.id
         LEFT JOIN oem_advertisement_linkings oal ON oa.project_id = oal.project_id
WHERE oa.is_deleted = false AND oa.is_mail_duplicate = false AND oa.is_tel_duplicate = false AND oa.blacklist1_id IS NULL AND oa.blacklist2_id IS NULL
ORDER BY oa.lst_status_change_date_time DESC, oa.created_at DESC;

CREATE OR REPLACE VIEW `operator_applicants_count_view` AS
SELECT
    oa.id,
    oa.parent_id,
    s.flow_type as selection_status_type,
    oa.project_id,
    oal.advertisement_id,
    oa.created_at,
    oa.lst_status_change_date_time
FROM operator_applicants oa
         LEFT JOIN selection_statuses s ON oa.selection_status_id = s.id
         LEFT JOIN operator_advertisement_linkings oal ON oa.project_id = oal.project_id
WHERE oa.is_deleted = false AND oa.is_mail_duplicate = false AND oa.is_tel_duplicate = false AND oa.blacklist1_id IS NULL AND oa.blacklist2_id IS NULL
ORDER BY oa.lst_status_change_date_time DESC, oa.created_at DESC;

CREATE OR REPLACE VIEW `operator_media_report_count_view` AS
SELECT ap.id as applicant_id,
       ad.start_date as ad_start_date,
       ap.created_at as applied_date,
       ss.flow_type as selection_status_type,
       ap.occupation as occupation,
       ap.hired_date,
       api.pic_id as interviewer_id,
       cl.client_name as interviewer_name,
       ap.pic_id as manager_id,
       educaP.client_name as manager_name,
       educaP.is_ini_education_staff,
       s.id as store_id,
       s.store_name as store_name,
       p.occupation_id as job_type_id,
       ms.status_name as job_type_name,
       m.id as media_id,
       m.media_name as media_name,
       ad.amount as advertisement_amount,
       ad.id as advertisement_id,
       m.hex_color,
       ap.parent_id
FROM operator_applicants ap
         LEFT JOIN operator_advertisements ad ON ad.master_media_id = ap.media_id
         LEFT JOIN master_medias m ON m.id = ad.master_media_id
         LEFT JOIN operator_applicant_interviews api ON api.applicant_id = ap.id
         LEFT JOIN operator_client_accounts cl ON cl.id = api.pic_id
         LEFT JOIN operator_client_accounts educaP ON educaP.id = ap.pic_id
         LEFT JOIN operator_projects p ON ap.project_id = p.id
         LEFT JOIN operator_stores s ON p.store_id = s.id
         LEFT JOIN selection_statuses ss ON ap.selection_status_id = ss.id
         LEFT JOIN master_statuses ms ON p.occupation_id = ms.id AND ms.type = 'OCCUPATION'
WHERE ap.is_deleted = false;

CREATE OR REPLACE VIEW `oem_media_report_count_view` AS
SELECT ap.id as applicant_id,
       ad.start_date as ad_start_date,
       ap.created_at as applied_date,
       ss.flow_type as selection_status_type,
       ap.occupation as occupation,
       ap.hired_date,
       api.pic_id as interviewer_id,
       cl.client_name as interviewer_name,
       ap.pic_id as manager_id,
       educaP.client_name as manager_name,
       educaP.is_ini_education_staff,
       s.id as store_id,
       s.store_name as store_name,
       p.occupation_id as job_type_id,
       ms.status_name as job_type_name,
       m.id as media_id,
       m.media_name as media_name,
       ad.amount as advertisement_amount,
       ad.id as advertisement_id,
       m.hex_color,
       ap.parent_id,
       ap.oem_group_id
FROM oem_applicants ap
         LEFT JOIN oem_advertisements ad ON ad.master_media_id = ap.media_id
         LEFT JOIN master_medias m ON m.id = ad.master_media_id
         LEFT JOIN oem_applicant_interviews api ON api.applicant_id = ap.id
         LEFT JOIN oem_client_accounts cl ON cl.id = api.pic_id
         LEFT JOIN oem_client_accounts educaP ON educaP.id = ap.pic_id
         LEFT JOIN oem_projects p ON ap.project_id = p.id
         LEFT JOIN oem_stores s ON p.store_id = s.id
         LEFT JOIN selection_statuses ss ON ap.selection_status_id = ss.id
         LEFT JOIN master_statuses ms ON p.occupation_id = ms.id AND ms.type = 'OCCUPATION'
WHERE ap.is_deleted = false;

