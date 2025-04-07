package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.exception.ApplianceNotFoundException;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.service.impl.ApplianceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplianceServiceImplTest {

    @Mock
    private ApplianceRepository applianceRepository;
    @Mock
    private ApplianceDTOMapper applianceDTOMapper;

    @InjectMocks
    private ApplianceServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAppliances() {
        Pageable pageable = PageRequest.of(0, 5);
        Appliance appliance = new Appliance();
        ApplianceDTO dto = new ApplianceDTO(
                1L,
                "TestName",
                Category.SMALL,
                "TestModel",
                null,
                PowerType.AC110,
                "-----",
                "TestDesc",
                Integer.valueOf(77),
                BigDecimal.valueOf(100));
        Page<Appliance> appliancePage = new PageImpl<>(Collections.singletonList(appliance));

        when(applianceRepository.findAll(pageable)).thenReturn(appliancePage);
        when(applianceDTOMapper.apply(appliance)).thenReturn(dto);

        Page<ApplianceDTO> result = service.getAppliances(pageable);
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(applianceRepository).findAll(pageable);
    }

    @Test
    void testCreateAppliance() {
        Appliance appliance = new Appliance();
        when(applianceRepository.save(appliance)).thenReturn(appliance);
        Appliance saved = service.createAppliance(appliance);
        assertEquals(appliance, saved);
        verify(applianceRepository).save(appliance);
    }

    @Test
    void testGetApplianeByIdFound() {
        Long id = 1L;
        Appliance appliance = new Appliance();
        ApplianceDTO dto = new ApplianceDTO(1L,
                "TestName",
                Category.SMALL,
                "TestModel",
                null,
                PowerType.AC110,
                "-----",
                "TestDesc",
                Integer.valueOf(77),
                BigDecimal.valueOf(100));
        when(applianceRepository.findById(id)).thenReturn(Optional.of(appliance));
        when(applianceDTOMapper.apply(appliance)).thenReturn(dto);

        ApplianceDTO result = service.getApplianeById(id);
        assertEquals(dto, result);
    }

    @Test
    void testGetApplianeByIdNotFound() {
        Long id = 1L;
        when(applianceRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ApplianceNotFoundException.class, () -> service.getApplianeById(id));
    }

    @Test
    void testDeleteAppliance() {
        Long id = 1L;
        service.deleteAppliance(id);
        verify(applianceRepository).deleteById(id);
    }

    @Test
    void testGetAllAppliancesList() {
        Appliance appliance1 = new Appliance();
        Appliance appliance2 = new Appliance();
        ApplianceDTO dto1 = new ApplianceDTO(1L,
                "TestName",
                Category.SMALL,
                "TestModel",
                null,
                PowerType.AC110,
                "-----",
                "TestDesc",
                Integer.valueOf(77),
                BigDecimal.valueOf(100));
        ApplianceDTO dto2 = new ApplianceDTO(1L,
                "TestName",
                Category.SMALL,
                "TestModel",
                null,
                PowerType.AC110,
                "-----",
                "TestDesc",
                Integer.valueOf(77),
                BigDecimal.valueOf(100));

        when(applianceRepository.findAll()).thenReturn(Arrays.asList(appliance1, appliance2));
        when(applianceDTOMapper.apply(appliance1)).thenReturn(dto1);
        when(applianceDTOMapper.apply(appliance2)).thenReturn(dto2);

        java.util.List<ApplianceDTO> list = service.getAllAppliancesList();
        assertEquals(2, list.size());
    }

    @Test
    void testSearchAppliances() {
        String search = "test";
        Pageable pageable = PageRequest.of(0, 5);
        Appliance appliance = new Appliance();
        ApplianceDTO dto = new ApplianceDTO(1L,
                "TestName",
                Category.SMALL,
                "TestModel",
                null,
                PowerType.AC110,
                "-----",
                "TestDesc",
                Integer.valueOf(77),
                BigDecimal.valueOf(100));
        Page<Appliance> page = new PageImpl<>(Collections.singletonList(appliance));

        when(applianceRepository.findByNameContainingIgnoreCase(search, pageable)).thenReturn(page);
        when(applianceDTOMapper.apply(appliance)).thenReturn(dto);

        Page<ApplianceDTO> result = service.searchAppliances(search, pageable);
        assertEquals(1, result.getContent().size());
    }
}
