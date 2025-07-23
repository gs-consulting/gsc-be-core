package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.dtos.BlacklistInfoDto;
import jp.co.goalist.gsc.entities.Blacklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, String> {

    @Query(value = """
            SELECT new jp.co.goalist.gsc.dtos.BlacklistInfoDto(b.id, b.fullName, b.tel)
            FROM blacklists b WHERE b.parentId = :parentId
            AND (case when (:oemGroupId is null) then (b.oemGroupId is null) else (b.oemGroupId = :oemGroupId) end)
            """)
    List<BlacklistInfoDto> findAllNameAndTelBlacklist(String parentId, String oemGroupId);

    @Query(value = """
            SELECT new jp.co.goalist.gsc.dtos.BlacklistInfoDto(b.id, b.fullName, b.tel)
            FROM blacklists b WHERE b.parentId = :parentId
            AND (case when (:oemGroupId is null) then (b.oemGroupId is null) else (b.oemGroupId = :oemGroupId) end)
            """)
    List<BlacklistInfoDto> findAllNameAndEmailBlacklist(String parentId, String oemGroupId);

    @Query(value = """
            select b.id from blacklists b where
            b.fullName = :name and b.tel = :tel
            order by b.createdAt desc
            """)
    Optional<String> findByFullNameAndTel(String name, String tel);

    @Query(value = """
            select b.id from blacklists b where
            b.fullName = :name and b.email = :email
            order by b.createdAt desc
            """)
    Optional<String> findByFullNameAndEmail(String name, String email);

    @Query(value = """
            FROM blacklists b WHERE b.parentId = :parentId
            AND (case when (:oemGroupId is null) then (b.oemGroupId is null) else (b.oemGroupId = :oemGroupId) end)
            """)
    Page<Blacklist> findAllByParentIdAndOemGroupId(String parentId, String oemGroupId, Pageable pageable);

    @Query(value = """
            FROM blacklists b WHERE
            ((b.fullName = :name AND b.tel = :tel)
            OR (b.fullName = :name AND b.email = :email))
            AND (case when (:oemGroupId is null) then (b.oemGroupId is null) else (b.oemGroupId = :oemGroupId) end)
            """)
    Optional<Blacklist> findAllByApplicantInfo(String name, String tel, String email, String parentId, String oemGroupId);

    @Modifying
    @Query(value = """
            DELETE FROM blacklists b WHERE b.parentId = :parentId
            AND (case when (:oemGroupId is null) then (b.oemGroupId is null) else (b.oemGroupId = :oemGroupId) end)
            AND b.id in (:ids)
            """)
    void deleteByParentAndIdIn(String parentId, String oemGroupId, List<String> ids);
}
