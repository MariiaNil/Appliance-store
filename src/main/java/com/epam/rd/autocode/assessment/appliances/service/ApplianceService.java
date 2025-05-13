package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ApplianceService {
    Page<ApplianceDTO> getAppliances(Pageable pageable);
    Appliance createAppliance(Appliance appliance);
    ApplianceDTO getApplianeById(Long id);
    void deleteAppliance(Long id);
    List<ApplianceDTO> getAllAppliancesList();

    Page<ApplianceDTO> searchAppliances(String search, Pageable pageable);
    Page<ApplianceDTO> getByCategory(Category category, Pageable pageable);
}
