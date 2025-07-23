package jp.co.goalist.gsc.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByIdAndRole(String id, String role);

}
