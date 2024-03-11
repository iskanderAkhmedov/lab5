package org.example.storage.repository.corporatesettlement;

import org.example.storage.model.corporatesettlement.AccountEntity;
import org.example.storage.model.corporatesettlement.AccountPoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findFirstByAccountPoolAndBussy(AccountPoolEntity accountPool, Boolean bussy);

    List<AccountEntity> findAllByAccountPool(AccountPoolEntity accountPool);
}
