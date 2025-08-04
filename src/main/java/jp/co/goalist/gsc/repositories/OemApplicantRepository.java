package jp.co.goalist.gsc.repositories;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.dtos.applicant.ApplicantClientAccountCountDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantDashboardCountDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantTotalCountDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantWithLatestMessagesDto;
import jp.co.goalist.gsc.entities.OemApplicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OemApplicantRepository extends JpaRepository<OemApplicant, String> {

    @Query(value = """
            SELECT b.id AS branchId,
            a.parent_id as parentId,
            COUNT(*) AS totalApplicants,
            COUNT(CASE
            		  WHEN a.lst_status_change_date_time is NULL 
            		    OR a.lst_status_change_date_time < CURRENT_TIMESTAMP - INTERVAL 24 HOUR
            		  THEN 1
            		  ELSE NULL
            	 END) AS totalUnResponseApplicants
            FROM oem_applicants a
            JOIN oem_projects p ON a.project_id = p.id
            JOIN oem_branches b ON p.branch_id = b.id
            WHERE a.is_deleted = false AND (:parentId IS NULL OR a.parent_id = :parentId) AND a.oem_group_id = :oemGroupId
            GROUP BY a.parent_id, b.id
            """, nativeQuery = true)
    List<ApplicantClientAccountCountDto> countAllApplicationsForClientList(String parentId, String oemGroupId);

    @Query(value = """
            (SELECT b.id AS branchId,
            a.parent_id as parentId,
            COUNT(*) AS totalApplicants,
            COUNT(CASE
            		  WHEN a.lst_status_change_date_time is NULL 
            		    OR a.lst_status_change_date_time < CURRENT_TIMESTAMP - INTERVAL 24 HOUR
            		  THEN 1
            		  ELSE NULL
            	 END) AS totalUnResponseApplicants
            FROM operator_applicants a
            JOIN operator_projects p ON a.project_id = p.id
            JOIN operator_branches b ON p.branch_id = b.id
            JOIN operator_client_locations loc ON loc.branch_id = b.id
            JOIN operator_client_accounts oca ON loc.operator_client_id = oca.id
            WHERE a.is_deleted = false AND oca.oem_group_id = :oemGroupId
            GROUP BY a.parent_id, b.id)
            UNION ALL
            (SELECT b.id AS branchId,
            a.parent_id as parentId,
            COUNT(*) AS totalApplicants,
            COUNT(CASE
            		  WHEN a.lst_status_change_date_time is NULL 
            		    OR a.lst_status_change_date_time < CURRENT_TIMESTAMP - INTERVAL 24 HOUR
            		  THEN 1
            		  ELSE NULL
            	 END) AS totalUnResponseApplicants
            FROM oem_applicants a
            JOIN oem_projects p ON a.project_id = p.id
            JOIN oem_branches b ON p.branch_id = b.id
            WHERE a.is_deleted = false AND a.oem_group_id = :oemGroupId
            GROUP BY a.parent_id, b.id)
            """, nativeQuery = true)
    List<ApplicantClientAccountCountDto> countAllApplicationsForClientListForOemList(String oemGroupId);

    @Query(value = """
            SELECT COUNT(*) FROM oem_applicants a
            WHERE a.is_deleted = false AND a.parent_id = :parentId AND a.oem_group_id = :oemGroupId
            AND (
                (a.lst_status_change_date_time IS NULL OR a.lst_status_change_date_time < CURRENT_TIMESTAMP - INTERVAL 24 HOUR)
                OR (:includeToday = true AND DATE(a.created_at) = CURRENT_DATE)
            )
            """, nativeQuery = true)
    Integer countStatusNotChangedApplications(String parentId, String oemGroupId, boolean includeToday);

    Optional<OemApplicant> findByIdAndParentIdAndOemGroupId(String id, String parentId, String oemGroupId);

    @Query("""
            FROM oem_applicants a
            WHERE a.parent.id = :parentId AND a.oemGroupId = :oemGroupId
            AND a.id IN :ids
            ORDER BY a.createdAt DESC
            """)
    List<OemApplicant> findAllApplicantsByIds(List<String> ids, String parentId, String oemGroupId);

    @Query("""
            FROM oem_applicants a
            WHERE a.project.id IN :ids
            """)
    List<OemApplicant> findAllApplicantsByProjectIds(List<String> ids);

    @Query("""
            FROM oem_applicants a
            WHERE a.parent.id = :parentId AND a.oemGroupId = :oemGroupId
            AND a.mediaApplicantId IN :ids
            ORDER BY a.createdAt DESC
            """)
    List<OemApplicant> findAllApplicantsByMediaApplicantIds(List<String> ids, String parentId, String oemGroupId);

    @Query(value = """
            SELECT 
            -- (today)
            CAST(IFNULL(SUM(CASE WHEN DATE(a.created_at) = CURRENT_DATE AND a.selection_status_type = 1 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN DATE(a.lst_status_change_date_time) = CURRENT_DATE AND a.selection_status_type >= 2 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN DATE(a.lst_status_change_date_time) = CURRENT_DATE AND a.selection_status_type >= 3 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN DATE(a.lst_status_change_date_time) = CURRENT_DATE AND a.selection_status_type = 4 THEN 1 ELSE 0 END), 0) AS SIGNED),
                
            -- (last 30 days)
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_SUB(CURRENT_DATE, INTERVAL 29 DAY) AND a.selection_status_type = 1 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_SUB(CURRENT_DATE, INTERVAL 29 DAY) AND a.selection_status_type = 2 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_SUB(CURRENT_DATE, INTERVAL 29 DAY) AND a.selection_status_type = 3 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_SUB(CURRENT_DATE, INTERVAL 29 DAY) AND a.selection_status_type = 4 THEN 1 ELSE 0 END), 0) AS SIGNED),
                
            -- (last month)
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01') 
                                 AND a.created_at < DATE_FORMAT(CURRENT_DATE, '%Y-%m-01') 
                                 AND a.selection_status_type = 1 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01') 
                                 AND a.created_at < DATE_FORMAT(CURRENT_DATE, '%Y-%m-01') 
                                 AND a.selection_status_type = 2 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01') 
                                 AND a.created_at < DATE_FORMAT(CURRENT_DATE, '%Y-%m-01') 
                                 AND a.selection_status_type = 3 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01') 
                                 AND a.created_at < DATE_FORMAT(CURRENT_DATE, '%Y-%m-01') 
                                 AND a.selection_status_type = 4 THEN 1 ELSE 0 END), 0) AS SIGNED),
                
            -- (2 months ago)
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 2 MONTH), '%Y-%m-01') 
                                 AND a.created_at < DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01') 
                                 AND a.selection_status_type = 1 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 2 MONTH), '%Y-%m-01') 
                                 AND a.created_at < DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01') 
                                 AND a.selection_status_type = 2 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 2 MONTH), '%Y-%m-01') 
                                 AND a.created_at < DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01') 
                                 AND a.selection_status_type = 3 THEN 1 ELSE 0 END), 0) AS SIGNED),
            CAST(IFNULL(SUM(CASE WHEN a.created_at >= DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 2 MONTH), '%Y-%m-01') 
                                 AND a.created_at < DATE_FORMAT(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01') 
                                 AND a.selection_status_type = 4 THEN 1 ELSE 0 END), 0) AS SIGNED)
            FROM (
                SELECT selection_status_type, created_at, lst_status_change_date_time FROM oem_applicants_count_view
                WHERE parent_id = :parentId AND oem_group_id = :oemGroupId
                GROUP BY id
            ) a
            """, nativeQuery = true)
    ApplicantDashboardCountDto countApplicantsForDashboard(String parentId, String oemGroupId);

    @Query(value = """
            SELECT NULL,
                CAST(IFNULL(SUM(CASE WHEN a.selection_status_type = 1 THEN 1 ELSE 0 END), 0) AS SIGNED),
                CAST(IFNULL(SUM(CASE WHEN a.selection_status_type = 2 THEN 1 ELSE 0 END), 0) AS SIGNED),
                CAST(IFNULL(SUM(CASE WHEN a.selection_status_type = 3 THEN 1 ELSE 0 END), 0) AS SIGNED),
                CAST(IFNULL(SUM(CASE WHEN a.selection_status_type = 4 THEN 1 ELSE 0 END), 0) AS SIGNED)
            FROM (
                SELECT selection_status_type FROM oem_applicants_count_view
                WHERE parent_id = :parentId AND oem_group_id = :oemGroupId AND project_id = :projectId
                GROUP BY id
            ) a
            """, nativeQuery = true)
    ApplicantTotalCountDto countApplicantsForProjectById(String parentId, String oemGroupId, String projectId);

    @Query(value = """
            SELECT a.project_id,
                CAST(IFNULL(SUM(CASE WHEN a.selection_status_type = 1 THEN 1 ELSE 0 END), 0) AS SIGNED),
                CAST(IFNULL(SUM(CASE WHEN a.selection_status_type = 2 THEN 1 ELSE 0 END), 0) AS SIGNED),
                CAST(IFNULL(SUM(CASE WHEN a.selection_status_type = 3 THEN 1 ELSE 0 END), 0) AS SIGNED),
                CAST(IFNULL(SUM(CASE WHEN a.selection_status_type = 4 THEN 1 ELSE 0 END), 0) AS SIGNED)
            FROM (
                SELECT project_id, selection_status_type FROM oem_applicants_count_view
                WHERE parent_id = :parentId AND oem_group_id = :oemGroupId AND project_id IS NOT NULL
                GROUP BY id
            ) a GROUP BY a.project_id
            """, nativeQuery = true)
    List<ApplicantTotalCountDto> countAllApplicantsForProject(String parentId, String oemGroupId);

    @Query("""
            FROM oem_applicants a
            WHERE a.parent.id = :parentId and a.oemGroupId = :oemGroupId
            ORDER BY a.createdAt DESC
            LIMIT 4
            """)
    List<OemApplicant> findLatestApplicantsForDashboard(String parentId, String oemGroupId);

    @Query("""
            SELECT a.id AS id,
                   a.fullName AS fullName,
                   a.gender AS gender,
                   a.birthday AS birthday,
                   CONCAT(p.name, " ", a.city) as address,
                   a.lstContactDateTime as repliedDate
            FROM oem_applicants a
            LEFT JOIN prefectures p ON a.prefecture.id = p.id
            WHERE a.lstContactDateTime IS NOT NULL AND a.isUnread = true AND a.parent.id = :parentId AND a.oemGroupId = :oemGroupId
            ORDER BY a.lstContactDateTime DESC
            LIMIT 5
            """)
    List<ApplicantWithLatestMessagesDto> findLatestUnreadChatForDashboard(String parentId, String oemGroupId);

    @Query("""
            SELECT a FROM oem_applicants a
            LEFT JOIN a.media m
            LEFT JOIN a.pic p
            LEFT JOIN p.account acc
            LEFT JOIN selection_statuses s ON s.id = a.selectionStatus.id
            WHERE a.project.id = :projectId AND s.flowType = :flowType
            AND a.parent.id = :parentId AND a.oemGroupId = :oemGroupId
            AND a.isMailDuplicate = false AND a.isTelDuplicate = false
            AND a.blacklist1 IS NULL AND a.blacklist2 IS NULL
            """)
    Page<OemApplicant> findAllApplicantsByFlowTypeForProject(String projectId, String flowType, String parentId, String oemGroupId, Pageable pageable);

    @Modifying
    @Query(value = """
            UPDATE oem_applicants oa
            SET oa.blacklist1_id = CASE WHEN oa.blacklist1_id IN :blacklistIds THEN NULL ELSE oa.blacklist1_id END,
                oa.blacklist2_id = CASE WHEN oa.blacklist2_id IN :blacklistIds THEN NULL ELSE oa.blacklist2_id END
            WHERE oa.parent_id = :parentId AND oa.oem_group_id = :oemGroupId
            """, nativeQuery = true)
    void updateBlacklistIdsToNull(List<String> blacklistIds, String parentId, String oemGroupId);

    @Modifying
    @Query(value = """
            UPDATE oem_applicants oa
            SET
                oa.blacklist1 = CASE
                    WHEN :tel IS NOT NULL AND oa.fullName = :name AND oa.tel = :tel THEN :blacklistId
                    ELSE oa.blacklist1
                END,
                oa.blacklist2 = CASE
                    WHEN :email IS NOT NULL AND oa.fullName = :name AND oa.email = :email THEN :blacklistId
                    ELSE oa.blacklist2
                END
            WHERE
                oa.fullName = :name
                AND ((:tel IS NOT NULL AND oa.tel = :tel) OR (:email IS NOT NULL AND oa.email = :email))
                AND oa.parent.id = :parentId
                AND oa.oemGroupId = :oemGroupId
            """)
    void updateBlacklistIdByApplicantInfo(String blacklistId, String parentId, String oemGroupId, String name, String tel, String email);

    @Transactional
    @Modifying
    @Query(value = """
            WITH ranked_applicants AS (
            SELECT
                id,
                ROW_NUMBER() OVER (PARTITION BY full_name, email ORDER BY created_at ASC) AS rn
            FROM oem_applicants
            WHERE email IS NOT NULL AND is_deleted = false
            )
            UPDATE oem_applicants oa
            JOIN ranked_applicants ra ON oa.id = ra.id
            SET oa.is_mail_duplicate = IF(ra.rn = 1, FALSE, TRUE)
            """, nativeQuery = true)
    void updateMailDuplicate();

    @Transactional
    @Modifying
    @Query(value = """
            WITH ranked_applicants AS (
            SELECT
                id,
                ROW_NUMBER() OVER (PARTITION BY full_name, tel ORDER BY created_at ASC) AS rn
            FROM oem_applicants
            WHERE tel IS NOT NULL AND is_deleted = false
            )
            UPDATE oem_applicants oa
            JOIN ranked_applicants ra ON oa.id = ra.id
            SET oa.is_tel_duplicate = IF(ra.rn = 1, FALSE, TRUE)
            """, nativeQuery = true)
    void updateTelDuplicate();

    @Query(value = "SELECT a.id FROM oem_applicants a WHERE a.id IN (:applicantIds) AND a.parent_id = :parentId AND a.oem_group_id = :oemGroupId", nativeQuery = true)
    List<String> findApplicantIdsByIds(List<String> applicantIds, String parentId, String oemGroupId);

    @Modifying
    @Query(value = "DELETE FROM oem_applicants WHERE id IN (:applicantIds) AND parent_id = :parentId AND oem_group_id = :oemGroupId", nativeQuery = true)
    void deleteApplicantsByIds(List<String> applicantIds, String parentId, String oemGroupId);

}
