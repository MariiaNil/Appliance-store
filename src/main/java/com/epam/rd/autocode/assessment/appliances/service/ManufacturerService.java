package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface ManufacturerService {
    Page<ManufacturerDTO> getAllManufacturers(Pageable pageable);
    Manufacturer createManufacturer(Manufacturer manufacturer);
    void deleteManufacturer(Long id);

    Optional<Manufacturer> getById(Long id);

    Page<ManufacturerDTO> searchManufacturers(String search, Pageable pageable);
}
