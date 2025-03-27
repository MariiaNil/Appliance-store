package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ManufacturerDTOMapper implements Function<Manufacturer, ManufacturerDTO> {
    @Override
    public ManufacturerDTO apply(Manufacturer manufacturer) {
        return new ManufacturerDTO(
                manufacturer.getId(),
                manufacturer.getName()
        );
    }
}
