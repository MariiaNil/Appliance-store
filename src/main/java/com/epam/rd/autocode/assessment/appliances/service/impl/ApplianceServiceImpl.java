package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.exception.ApplianceNotFoundException;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;
    private final ApplianceDTOMapper applianceDTOMapper;

    @Loggable
    @Override
    @PermitAll
    public Page<ApplianceDTO> getAppliances(Pageable pageable) {
        Page<Appliance> appliancePage = applianceRepository.findAll(pageable);
        return appliancePage.map(applianceDTOMapper);
    }

    @Loggable
    @Override
    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Appliance createAppliance(Appliance appliance) {
        return applianceRepository.save(appliance);
    }

    @Loggable
    @Override
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CLIENT')")
    public ApplianceDTO getApplianeById(Long id) {
        return applianceRepository.findById(id)
                .map(applianceDTOMapper)
                .orElseThrow(() ->
                    new ApplianceNotFoundException("Appliance not found"));

    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void deleteAppliance(Long id) {
        applianceRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<ApplianceDTO> getAllAppliancesList() {
        return applianceRepository.findAll()
                .stream()
                .map(applianceDTOMapper)
                .toList();
    }

    @Override
    @PermitAll
    public Page<ApplianceDTO> searchAppliances(String search, Pageable pageable) {
        return applianceRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(applianceDTOMapper);
    }
}
