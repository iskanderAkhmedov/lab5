package org.example.storage.repository.corporatesettlement;

import org.example.storage.model.corporatesettlement.TppRefAccountTypeEntity;
import org.example.storage.model.corporatesettlement.TppRefProductClassEntity;
import org.example.storage.model.corporatesettlement.TppRefProductRegisterTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TppRefProductRegisterTypeRepository extends JpaRepository<TppRefProductRegisterTypeEntity, Long> {

    Optional<TppRefProductRegisterTypeEntity> findByValue(String value);

    List<TppRefProductRegisterTypeEntity> findAllByProductClassAndAccountType(TppRefProductClassEntity productClass, TppRefAccountTypeEntity accountType);
}
