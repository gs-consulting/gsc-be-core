package jp.co.goalist.gsc.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.OemTemplate;

@Repository
public interface OemTemplateRepository extends JpaRepository<OemTemplate, String> {

    @Query("from oem_templates where id = :id and parentId = :parentId and oemGroupId = :oemGroupId")
    Optional<OemTemplate> findById(String id, String parentId, String oemGroupId);

    @Modifying
    @Query(value = "delete from oem_templates where parent_id = :parentId and oem_group_id = :oemGroupId and id in (:ids)", nativeQuery = true)
    void deleteByParentAndIdIn(List<String> ids, String parentId, String oemGroupId);

    @Query("from oem_templates where parentId = :parentId and oemGroupId = :oemGroupId and (:searchInput is null or templateName like :searchInput)")
    Page<OemTemplate> findAllBy(String parentId, String oemGroupId, String searchInput, Pageable pageable);
}
