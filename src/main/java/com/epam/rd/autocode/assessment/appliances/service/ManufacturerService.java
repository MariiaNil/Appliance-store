package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;

import java.util.List;

public interface ManufacturerService {
    public List<Manufacturer> getManufacturers();
    public Manufacturer createManufacturer(Manufacturer manufacturer);
    public Manufacturer getManufacturerById(Long id);
    public Manufacturer updateManufacturer(Long id, Manufacturer manufacturer);
    public void deleteManufacturer(Long id);
}
