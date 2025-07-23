package jp.co.goalist.gsc.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.MonthlyCost;

@Repository
public interface MonthlyCostRepository extends JpaRepository<MonthlyCost, String> {

    @Query(value = """
        FROM monthly_costs mc WHERE mc.startMonth = :startMonth
        AND mc.parentId = :parentId AND ((:oemGroupId IS NULL AND mc.oemGroupId IS NULL) OR mc.oemGroupId = :oemGroupId)
        """)
    List<MonthlyCost> findMonthlyCostSetting(String parentId, String oemGroupId, LocalDate startMonth);

    @Query(value = """
        FROM monthly_costs mc WHERE mc.startMonth = :startMonth
        AND mc.parentId = :parentId AND ((:oemGroupId IS NULL AND mc.oemGroupId IS NULL) OR mc.oemGroupId = :oemGroupId)
        AND mc.id NOT IN (:insertedIds)
        ORDER BY mc.id ASC
        """)
    List<MonthlyCost> getRemovedMonthlyCostSetting(String parentId, String oemGroupId, LocalDate startMonth, Set<String> insertedIds);
}
