package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;

import java.util.List;

public interface ApplianceService {
    public List<ApplianceDTO> getAppliances();
    public Appliance createAppliance(Appliance appliance);
    public ApplianceDTO getApplianeById(Long id);
    public ApplianceDTO updateAppliance(Long id,ApplianceDTO applianceDto);
    public void deleteAppliance(Long id);
}
