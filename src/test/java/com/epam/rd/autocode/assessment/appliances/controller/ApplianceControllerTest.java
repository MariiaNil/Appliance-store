package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplianceControllerTest {

    @Mock
    private ApplianceService applianceService;

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private Model model;

    @Mock
    private Pageable pageable;

    @Mock
    private Page<ApplianceDTO> appliancePage;

    @InjectMocks
    private ApplianceController controller;

    @Test
    void testListAppliancesWithoutSearch() {
        when(applianceService.getAppliances(any(Pageable.class))).thenReturn(appliancePage);

        String view = controller.listAppliances(model, null, pageable);

        verify(applianceService).getAppliances(pageable);
        verify(model).addAttribute("appliancesPage", appliancePage);
        verify(model).addAttribute("search", null);
        assertEquals("appliance/appliances", view);
    }

    @Test
    void testListAppliancesWithSearch() {
        String search = "some query";
        when(applianceService.searchAppliances(eq(search), any(Pageable.class))).thenReturn(appliancePage);

        String view = controller.listAppliances(model, search, pageable);

        verify(applianceService).searchAppliances(eq(search), eq(pageable));
        verify(model).addAttribute("appliancesPage", appliancePage);
        verify(model).addAttribute("search", search);
        assertEquals("appliance/appliances", view);
    }

    @Test
    void testShowAddApplianceForm() {
        // Подготавливаем страницу с производителями (можно создать мок страницы)
        when(manufacturerService.getAllManufacturers(any(Pageable.class))).thenReturn(new org.springframework.data.domain.PageImpl<>(List.of()));

        String view = controller.showAddApplianceForm(model, 100);

        verify(model).addAttribute(eq("categories"), any());
        verify(model).addAttribute(eq("powerTypes"), any());
        verify(model).addAttribute(eq("manufacturers"), any());
        verify(model).addAttribute(eq("appliance"), any(Appliance.class));
        assertEquals("appliance/newAppliance", view);
    }

    @Test
    void testAddAppliance() {
        Appliance appliance = new Appliance();
        String view = controller.addAppliance(appliance);

        verify(applianceService).createAppliance(appliance);
        assertEquals("redirect:/appliances", view);
    }
}
