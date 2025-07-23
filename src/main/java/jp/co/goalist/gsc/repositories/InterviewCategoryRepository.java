package jp.co.goalist.gsc.repositories;

import java.util.List;
import java.util.Set;

import jp.co.goalist.gsc.dtos.InterviewCategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.InterviewCategory;

@Repository
public interface InterviewCategoryRepository extends JpaRepository<InterviewCategory, String> {

    @Query(value = """
        SELECT new jp.co.goalist.gsc.dtos.InterviewCategoryDto(
        ic.id, ic.categoryName, ic.order, CASE WHEN app_interview.interviewId IS NULL THEN true ELSE false END
        )
        FROM interview_categories ic LEFT JOIN
        (SELECT DISTINCT i.category.id AS interviewId FROM operator_applicant_interviews i GROUP BY i.category.id) AS app_interview
        ON ic.id = app_interview.interviewId
        WHERE ic.parentId = :parentId AND ic.oemGroupId IS NULL
        ORDER BY ic.order ASC
    """)
    List<InterviewCategoryDto> getOperatorInterviewCategories(String parentId);

    @Query(value = """
        SELECT new jp.co.goalist.gsc.dtos.InterviewCategoryDto(
        ic.id, ic.categoryName, ic.order, CASE WHEN app_interview.interviewId IS NULL THEN true ELSE false END
        )
        FROM interview_categories ic LEFT JOIN
        (SELECT DISTINCT i.category.id AS interviewId FROM oem_applicant_interviews i GROUP BY i.category.id) AS app_interview
        ON ic.id = app_interview.interviewId
        WHERE ic.parentId = :parentId AND ic.oemGroupId = :oemGroupId
        ORDER BY ic.order ASC
    """)
    List<InterviewCategoryDto> getOemInterviewCategories(String parentId, String oemGroupId);

    @Query(value = """
            FROM interview_categories ic
            WHERE ic.parentId = :parentId AND ((:oemGroupId IS NULL AND ic.oemGroupId IS NULL) OR ic.oemGroupId = :oemGroupId)
            AND ic.id NOT IN (:insertedIds)
            ORDER BY ic.order ASC
            """)
    List<InterviewCategory> getRemovedInterviewCategory(String parentId, String oemGroupId, Set<String> insertedIds);
}
