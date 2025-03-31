package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ApplianceService {
    Page<ApplianceDTO> getAppliances(Pageable pageable);
    Appliance createAppliance(Appliance appliance);
    ApplianceDTO getApplianeById(Long id);
    ApplianceDTO updateAppliance(Long id,ApplianceDTO applianceDto);
    void deleteAppliance(Long id);
    List<ApplianceDTO> getAllAppliancesList();

    Page<ApplianceDTO> searchAppliances(String search, Pageable pageable);
}
