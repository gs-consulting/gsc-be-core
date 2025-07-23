package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.Prefecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrefectureRepository extends JpaRepository<Prefecture, Integer> {

    @Query(value = "SELECT p FROM prefectures p JOIN FETCH p.cities ORDER BY p.id asc")
    List<Prefecture> getPrefecturesOrderById();

    @Query(value = "SELECT p FROM prefectures p where p.id in (:ids)")
    List<Prefecture> getPrefecturesBy(List<String> ids);
}