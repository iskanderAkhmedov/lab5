package org.example.storage.repository.corporatesettlement;

import org.example.storage.model.corporatesettlement.TppProductRegisterEntity;
import org.example.storage.model.corporatesettlement.TppRefProductRegisterTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TppProductRegisterRepository extends JpaRepository<TppProductRegisterEntity, Long> {

    Optional<TppProductRegisterEntity> findByProductIdAndType(Long productId, TppRefProductRegisterTypeEntity type);
}
