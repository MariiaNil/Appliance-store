package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ManufacturerDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.impl.ManufacturerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManufacturerServiceImplTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private ManufacturerDTOMapper manufacturerDTOMapper;

    @InjectMocks
    private ManufacturerServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllManufacturers() {
        Pageable pageable = PageRequest.of(0, 5);
        Manufacturer manufacturer = new Manufacturer();
        // Фиктивный ManufacturerDTO record (например, id и name)
        ManufacturerDTO dto = new ManufacturerDTO(1L, "TestManufacturer");
        Page<Manufacturer> page = new PageImpl<>(Collections.singletonList(manufacturer));

        when(manufacturerRepository.findAll(pageable)).thenReturn(page);
        when(manufacturerDTOMapper.apply(manufacturer)).thenReturn(dto);

        Page<ManufacturerDTO> result = service.getAllManufacturers(pageable);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testCreateManufacturer() {
        Manufacturer manufacturer = new Manufacturer();
        when(manufacturerRepository.save(manufacturer)).thenReturn(manufacturer);
        Manufacturer created = service.createManufacturer(manufacturer);
        assertEquals(manufacturer, created);
    }

    @Test
    void testDeleteManufacturer() {
        Long id = 1L;
        service.deleteManufacturer(id);
        verify(manufacturerRepository).deleteById(id);
    }

    @Test
    void testSearchManufacturers() {
        String search = "brand";
        Pageable pageable = PageRequest.of(0, 5);
        Manufacturer manufacturer = new Manufacturer();
        ManufacturerDTO dto = new ManufacturerDTO(1L, "TestManufacturer");
        Page<Manufacturer> page = new PageImpl<>(Collections.singletonList(manufacturer));

        when(manufacturerRepository.findByNameContainingIgnoreCase(search, pageable)).thenReturn(page);
        when(manufacturerDTOMapper.apply(manufacturer)).thenReturn(dto);

        Page<ManufacturerDTO> result = service.searchManufacturers(search, pageable);
        assertEquals(1, result.getContent().size());
    }
}
