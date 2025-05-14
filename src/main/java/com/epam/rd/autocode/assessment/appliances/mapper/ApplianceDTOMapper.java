package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.CategoryDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ApplianceDTOMapper implements Function<Appliance, ApplianceDTO> {
    @Override
    public ApplianceDTO apply(Appliance appliance) {
        return new ApplianceDTO(
                appliance.getId(),
                appliance.getName(),
                new CategoryDTOMapper().apply(appliance.getCategory()),
                appliance.getModel(),
                new ManufacturerDTOMapper().apply(appliance.getManufacturer()),
                appliance.getPowerType(),
                appliance.getCharacteristic(),
                appliance.getDescription(),
                appliance.getPower(),
                appliance.getPrice()
        );
    }
}
