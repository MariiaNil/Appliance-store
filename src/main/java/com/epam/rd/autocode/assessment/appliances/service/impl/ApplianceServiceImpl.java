package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;
    private final ApplianceDTOMapper applianceDTOMapper;

    @Override
    public Page<ApplianceDTO> getAppliances(Pageable pageable) {
        Page<Appliance> appliancePage = applianceRepository.findAll(pageable);
        return appliancePage.map(applianceDTOMapper);
    }

    @Override
    @Transactional
    public Appliance createAppliance(Appliance appliance) {
        return applianceRepository.save(appliance);
    }

    @Override
    public ApplianceDTO getApplianeById(Long id) {
        return applianceRepository.findById(id)
                .map(applianceDTOMapper)
                .orElseThrow(() -> new RuntimeException("Appliance not found"));
    }

    @Override
    public ApplianceDTO updateAppliance(Long id, ApplianceDTO applianceDto) {
        return null;
    }

    @Override
    public void deleteAppliance(Long id) {
        applianceRepository.deleteById(id);
    }

    @Override
    public Page<ApplianceDTO> searchAppliances(String search, Pageable pageable) {
        return applianceRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(applianceDTOMapper);
    }
}
