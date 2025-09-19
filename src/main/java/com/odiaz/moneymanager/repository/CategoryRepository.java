package com.odiaz.moneymanager.repository;

import com.odiaz.moneymanager.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    List<CategoryEntity> findByProfileId(Integer profileId);
    Optional<CategoryEntity> findByIdAndProfileId(Integer id, Integer profileId);

    List<CategoryEntity> findByTypeAndProfileId(String type, Integer profileId);

    Boolean existsByNameAndProfileId(String name, Integer profileId);
}
