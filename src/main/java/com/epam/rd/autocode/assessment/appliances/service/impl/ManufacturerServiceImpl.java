package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
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

    private static final Logger logger = LoggerFactory.getLogger(ManufacturerServiceImpl.class);
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerDTOMapper manufacturerDTOMapper;

    @Override
    public Page<ManufacturerDTO> getAllManufacturers(Pageable pageable) {
        Page<Manufacturer> manufacturerPage = manufacturerRepository.findAll(pageable);
        logger.info("Manufacturers found: {}", manufacturerPage.getTotalElements());
        return manufacturerPage.map(manufacturerDTOMapper);
    }

    @Override
    @Transactional
    public Manufacturer createManufacturer(Manufacturer manufacturer) {
        logger.info("Creating manufacturer: {}", manufacturer);
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public ManufacturerDTO getManufacturerById(Long id) {
        logger.info("Getting manufacturer by ID: {}", id);
        return manufacturerRepository.findById(id)
                .map(manufacturerDTOMapper)
                .orElseThrow(() -> {
                    logger.error("Manufacturer with ID {} not found", id);
                    return new RuntimeException("Manufacturer not found");
                });
    }

    @Override
    public ManufacturerDTO updateManufacturer(Long id, ManufacturerDTO manufacturerDto) {
        return null;
    }

    @Override
    public void deleteManufacturer(Long id) {
        manufacturerRepository.deleteById(id);
        logger.info("Manufacturer with ID {} deleted", id);
    }

    @Override
    public Page<ManufacturerDTO> searchManufacturers(String search, Pageable pageable) {
        logger.info("Searching manufacturers by name: {}", search);
        return manufacturerRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(manufacturerDTOMapper);
    }
}
