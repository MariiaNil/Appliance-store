package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.exception.ManufacturerNotFoundException;
import com.epam.rd.autocode.assessment.appliances.mapper.ManufacturerDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerDTOMapper manufacturerDTOMapper;

    @Override
    public Page<ManufacturerDTO> getAllManufacturers(Pageable pageable) {
        Page<Manufacturer> manufacturerPage = manufacturerRepository.findAll(pageable);
        return manufacturerPage.map(manufacturerDTOMapper);
    }

    @Override
    @Transactional
    public Manufacturer createManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public void deleteManufacturer(Long id) {
        manufacturerRepository.deleteById(id);
    }

    @Override
    public Page<ManufacturerDTO> searchManufacturers(String search, Pageable pageable) {
        return manufacturerRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(manufacturerDTOMapper);
    }
}
