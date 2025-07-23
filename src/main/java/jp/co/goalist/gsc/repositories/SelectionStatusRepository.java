package jp.co.goalist.gsc.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import jp.co.goalist.gsc.dtos.SelectionStatusDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.SelectionStatus;

@Repository
public interface SelectionStatusRepository extends JpaRepository<SelectionStatus, String> {

    @Query(value = """
            SELECT NEW jp.co.goalist.gsc.dtos.SelectionStatusDto(s.id, s.itemName, s.displayOrder, s.flowType,
             CASE WHEN (app.statusId IS NULL) THEN true ELSE false END)
            FROM selection_statuses s LEFT JOIN
             (SELECT a.selectionStatus.id AS statusId FROM operator_applicants a GROUP BY a.selectionStatus.id)
             AS app ON s.id = app.statusId
            WHERE s.parentId = :parentId AND s.oemGroupId IS NULL
            ORDER BY s.displayOrder ASC
            """)
    List<SelectionStatusDto> findAllOperatorStatuses(String parentId);

    @Query(value = """
             SELECT NEW jp.co.goalist.gsc.dtos.SelectionStatusDto(s.id, s.itemName, s.displayOrder, s.flowType,
             CASE WHEN (app.statusId IS NULL) THEN true ELSE false END)
            FROM selection_statuses s LEFT JOIN
             (SELECT a.selectionStatus.id AS statusId FROM oem_applicants a GROUP BY a.selectionStatus.id)
             AS app ON s.id = app.statusId
            WHERE s.parentId = :parentId AND s.oemGroupId = :oemGroupId
            ORDER BY s.displayOrder ASC
            """)
    List<SelectionStatusDto> findAllOemStatuses(String parentId, String oemGroupId);

    @Query(value = """
            from selection_statuses ss
            where ss.parentId = :parentId
            and (:oemGroupId is null or ss.oemGroupId = :oemGroupId)
            and ss.id = :id
            """)
    Optional<SelectionStatus> findStatusBy(String id, String oemGroupId, String parentId);

    @Query(value = """
            from selection_statuses ss
            where ss.parentId = :parentId
            and (:oemGroupId is null or ss.oemGroupId = :oemGroupId)
            and ss.id in (:ids)
            """)
    List<SelectionStatus> findAllStatusBy(List<String> ids, String parentId, String oemGroupId);

    @Query(value = """
            FROM selection_statuses s
            WHERE s.parentId = :parentId AND ((:oemGroupId IS NULL AND s.oemGroupId IS NULL) OR s.oemGroupId = :oemGroupId)
            AND s.id NOT IN (:insertedIds)
            ORDER BY s.displayOrder ASC
            """)
    List<SelectionStatus> getRemovedSelectionStatuses(String parentId, String oemGroupId, Set<String> insertedIds);
}
