package org.example.storage.repository.corporatesettlement;

import org.example.storage.model.corporatesettlement.TppProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TppProductRepository extends JpaRepository<TppProductEntity, Long> {

    List<TppProductEntity> findAllByNumber(String number);
}
