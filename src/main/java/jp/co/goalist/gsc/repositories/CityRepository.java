package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    @Query(value = "SELECT c FROM cities c where c.id in (:ids)")
    List<City> getCitiesBy(List<String> ids);
}
