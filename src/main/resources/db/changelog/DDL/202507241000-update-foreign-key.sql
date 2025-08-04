-- liquibase formatted sql
-- changeset gcs:202507241000-update-foreign-key

-- クライアント

ALTER TABLE `operator_client_locations` DROP FOREIGN KEY operator_client_locations_ibfk_1;
ALTER TABLE `operator_client_locations` DROP FOREIGN KEY operator_client_locations_ibfk_2;
ALTER TABLE `operator_client_locations` DROP FOREIGN KEY operator_client_locations_ibfk_3;

ALTER TABLE `operator_client_locations`
    ADD CONSTRAINT fk_op_loc_client FOREIGN KEY (operator_client_id) REFERENCES operator_client_accounts(id) ON DELETE CASCADE;

ALTER TABLE `operator_client_locations`
    ADD CONSTRAINT fk_op_loc_branch FOREIGN KEY (branch_id) REFERENCES operator_branches(id) ON DELETE CASCADE;

ALTER TABLE `operator_client_locations`
    ADD CONSTRAINT fk_op_loc_store FOREIGN KEY (store_id) REFERENCES operator_stores(id) ON DELETE CASCADE;

ALTER TABLE `oem_client_locations` DROP FOREIGN KEY oem_client_locations_ibfk_1;
ALTER TABLE `oem_client_locations` DROP FOREIGN KEY oem_client_locations_ibfk_2;
ALTER TABLE `oem_client_locations` DROP FOREIGN KEY oem_client_locations_ibfk_3;

ALTER TABLE `oem_client_locations`
    ADD CONSTRAINT fk_oem_loc_client FOREIGN KEY (oem_client_id) REFERENCES oem_client_accounts(id) ON DELETE CASCADE;

ALTER TABLE `oem_client_locations`
    ADD CONSTRAINT fk_oem_loc_branch FOREIGN KEY (branch_id) REFERENCES oem_branches(id) ON DELETE CASCADE;

ALTER TABLE `oem_client_locations`
    ADD CONSTRAINT fk_oem_loc_store FOREIGN KEY (store_id) REFERENCES oem_stores(id) ON DELETE CASCADE;

-- 案件

ALTER TABLE `operator_projects` DROP FOREIGN KEY operator_projects_ibfk_1;
ALTER TABLE `operator_projects` DROP FOREIGN KEY operator_projects_ibfk_2;

ALTER TABLE `operator_projects`
    ADD CONSTRAINT fk_project_branch FOREIGN KEY (branch_id) REFERENCES operator_branches(id) ON DELETE SET NULL;

ALTER TABLE `operator_projects`
    ADD CONSTRAINT fk_project_store FOREIGN KEY (store_id) REFERENCES operator_stores(id) ON DELETE SET NULL;

ALTER TABLE `oem_projects` DROP FOREIGN KEY oem_projects_ibfk_1;
ALTER TABLE `oem_projects` DROP FOREIGN KEY oem_projects_ibfk_2;

ALTER TABLE `oem_projects`
    ADD CONSTRAINT fk_oem_branch FOREIGN KEY (branch_id) REFERENCES oem_branches(id) ON DELETE SET NULL;

ALTER TABLE `oem_projects`
    ADD CONSTRAINT fk_oem_store FOREIGN KEY (store_id) REFERENCES oem_stores(id) ON DELETE SET NULL;
