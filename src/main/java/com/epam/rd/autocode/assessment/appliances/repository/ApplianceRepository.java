package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplianceRepository extends JpaRepository<Appliance, Long> {
    Page<Appliance> findAll(Pageable pageable);
    Page<Appliance> findByNameContainingIgnoreCase(String search, Pageable pageable);

    Page<Appliance> findByCategory(Category category, Pageable pageable);
}
