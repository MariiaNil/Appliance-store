package com.epam.rd.autocode.assessment.appliances.repository;


import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Page<Manufacturer> findAll(Pageable pageable);
    Page<Manufacturer> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
