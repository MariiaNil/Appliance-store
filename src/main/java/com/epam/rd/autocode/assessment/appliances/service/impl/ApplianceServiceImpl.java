package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;

    @Override
    public List<Appliance> getAppliances() {
        return applianceRepository.findAll();
    }

    @Override
    public Appliance createAppliance(Appliance appliance) {
        return applianceRepository.save(appliance);
    }

    @Override
    public Appliance getApplianeById(Long id) {
        return applianceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appliance not found"));
    }

    @Override
    public Appliance updateAppliance(Long id, Appliance appliance) {
        return null;
    }

    @Override
    public void deleteAppliance(Long id) {
        applianceRepository.deleteById(id);
    }
}
