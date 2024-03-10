package org.example.storage.repository.corporatesettlement;

import org.example.storage.model.corporatesettlement.TppRefAccountTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TppRefAccountTypeRepository extends JpaRepository<TppRefAccountTypeEntity, Long> {

    Optional<TppRefAccountTypeEntity> findByValue(String value);
}
