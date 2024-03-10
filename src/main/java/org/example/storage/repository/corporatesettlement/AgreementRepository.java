package org.example.storage.repository.corporatesettlement;

import org.example.storage.model.corporatesettlement.AgreementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<AgreementEntity, Long> {
    List<AgreementEntity> findAllByNumberIn(List<String> number);
}
