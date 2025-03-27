package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;

import java.util.List;

public interface ApplianceService {
    public List<Appliance> getAppliances();
    public Appliance createAppliance(Appliance appliance);
    public Appliance getApplianeById(Long id);
    public Appliance updateAppliance(Long id,Appliance appliance);
    public void deleteAppliance(Long id);
}
