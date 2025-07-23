-- liquibase formatted sql
-- changeset gcs:202506181300-create-media-report-view

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
         LEFT JOIN master_statuses ms ON p.occupation_id = ms.id AND ms.type = 'OCCUPATION';

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
         LEFT JOIN master_statuses ms ON p.occupation_id = ms.id AND ms.type = 'OCCUPATION';
