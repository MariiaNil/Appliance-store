package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManufacturerControllerTest {

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private Model model;

    private final Pageable pageable = PageRequest.of(0, 5, Sort.by("id", "name"));

    // Используем реальный объект страницы для возвращаемого значения
    @SuppressWarnings("unchecked")
    private final Page<ManufacturerDTO> manufacturerPage = new PageImpl<>(Collections.emptyList());

    @InjectMocks
    private ManufacturerController controller;

    @Test
    void testListManufacturerWithoutSearch() {
        when(manufacturerService.getAllManufacturers(any(Pageable.class))).thenReturn(manufacturerPage);

        String view = controller.listManufacturer(model, null, pageable);

        verify(manufacturerService).getAllManufacturers(pageable);
        verify(model).addAttribute("manufacturerPage", manufacturerPage);
        verify(model).addAttribute("search", null);
        assertEquals("manufacture/manufacturers", view);
    }

    @Test
    void testListManufacturerWithSearch() {
        String search = "Samsung";
        when(manufacturerService.searchManufacturers(eq(search), any(Pageable.class))).thenReturn(manufacturerPage);

        String view = controller.listManufacturer(model, search, pageable);

        verify(manufacturerService).searchManufacturers(eq(search), eq(pageable));
        verify(model).addAttribute("manufacturerPage", manufacturerPage);
        verify(model).addAttribute("search", search);
        assertEquals("manufacture/manufacturers", view);
    }

    @Test
    void testShowAddManufacturerForm() {
        String view = controller.showAddManufacturerForm(model);

        verify(model).addAttribute(eq("manufacturer"), any(Manufacturer.class));
        assertEquals("manufacture/newManufacturer", view);
    }

    @Test
    void testCreateManufacturer() {
        Manufacturer manufacturer = new Manufacturer();
        String view = controller.createManufacturer(manufacturer);

        verify(manufacturerService).createManufacturer(manufacturer);
        assertEquals("redirect:/manufacturers", view);
    }
}
