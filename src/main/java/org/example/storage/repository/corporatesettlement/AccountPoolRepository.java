package org.example.storage.repository.corporatesettlement;

import org.example.storage.model.corporatesettlement.AccountPoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountPoolRepository extends JpaRepository<AccountPoolEntity, Long> {

    Optional<AccountPoolEntity> findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(
            String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode);
}
