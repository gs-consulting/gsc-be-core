package jp.co.goalist.gsc.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.OperatorTemplate;

@Repository
public interface OperatorTemplateRepository extends JpaRepository<OperatorTemplate, String> {

    @Query("from operator_templates where id = :id and parentId = :parentId")
    Optional<OperatorTemplate> findById(String id, String parentId);

    @Modifying
    @Query(value = "delete from operator_templates where parent_id = :parentId and id in (:ids)", nativeQuery = true)
    void deleteByParentAndIdIn(List<String> ids, String parentId);

    @Query("from operator_templates where parentId = :parentId and (:searchInput is null or templateName like :searchInput)")
    Page<OperatorTemplate> findAllBy(String parentId, String searchInput, Pageable pageable);
}
