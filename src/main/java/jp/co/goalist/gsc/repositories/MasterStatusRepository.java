package jp.co.goalist.gsc.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.MasterStatus;

@Repository
public interface MasterStatusRepository extends JpaRepository<MasterStatus, Long> {

    Page<MasterStatus> findByParentIdAndType(String parentId, String type, Pageable pageable);

    @Query(value = """
            from master_statuses ms
            where ms.parentId = :parentId and ms.type = :type
            and (:oemGroupId is null or ms.oemGroupId = :oemGroupId)
            and ms.id in (:ids)
            """)
    List<MasterStatus> findStatusByIds(List<Long> ids, String oemGroupId, String parentId, String type);

    @Query(value = """
            select ms.*
            from master_statuses ms
            where ms.parent_id = :parentId
            and (case when (:oemGroupId is null) then (ms.oem_group_id is null) else (ms.oem_group_id = :oemGroupId) end)
            or (coalesce(:statuses) is null or (ms.id in (:statuses) and ms.type = 'PROJECT_STATUS'))
            or (coalesce(:occupations) is null or (ms.id in (:occupations) and ms.type = 'OCCUPATION'))
            or (coalesce(:employmentStatues) is null or (ms.id in (:employmentStatues) and ms.type = 'EMPLOYMENT_TYPE'))
            or (coalesce(:workingPeriods) is null or (ms.id in (:workingPeriods) and ms.type = 'WORKING_PERIOD'))
            or (coalesce(:interviewVenues) is null or (ms.id in (:interviewVenues) and ms.type = 'INTERVIEW_LOC'))
            or (coalesce(:qualifications) is null or (ms.id in (:qualifications) and ms.type = 'QUALIFICATION'))
            or (coalesce(:experiences) is null or (ms.id in (:experiences) and ms.type = 'EXPERIENCE'))
            """,
            nativeQuery = true)
    List<MasterStatus> findMasterStatusForCSV(List<String> statuses,
                                              List<String> occupations,
                                              List<String> employmentStatues,
                                              List<String> workingPeriods,
                                              List<String> interviewVenues,
                                              List<String> qualifications,
                                              List<String> experiences,
                                              String parentId,
                                              String oemGroupId);

    @Query(value = """
            FROM master_statuses ms
            WHERE ms.parentId = :parentId AND ((:oemGroupId IS NULL AND ms.oemGroupId IS NULL) OR ms.oemGroupId = :oemGroupId)
            AND ms.type = :type
            AND ms.id NOT IN (:insertedIds)
            ORDER BY ms.order ASC
            """)
    List<MasterStatus> getRemovedMasterStatuses(String parentId, String oemGroupId, String type, Set<String> insertedIds);
}
