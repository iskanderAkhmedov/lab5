package org.example.storage.repository.corporatesettlement;

import org.example.storage.model.corporatesettlement.TppRefProductClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TppRefProductClassRepository extends JpaRepository<TppRefProductClassEntity, Long> {

    Optional<TppRefProductClassEntity> findByValue(String value);
}
