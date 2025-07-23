package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OemGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OemGroupRepository extends JpaRepository<OemGroup, String> {

    Optional<OemGroup> findByOemGroupName(String name);
}
