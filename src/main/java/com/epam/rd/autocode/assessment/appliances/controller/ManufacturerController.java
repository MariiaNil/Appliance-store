package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @GetMapping
    public String listManufacturer(
            Model model,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 5, sort = {"id", "name"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ManufacturerDTO> manufacturerPage;
        if (search != null && !search.trim().isEmpty()) {
            manufacturerPage = manufacturerService.searchManufacturers(search, pageable);
        } else {
            manufacturerPage = manufacturerService.getAllManufacturers(pageable);
        }
        model.addAttribute("manufacturerPage", manufacturerPage);
        model.addAttribute("search", search);
        return "manufacture/manufacturers";
    }

    @GetMapping("/add")
    public String showAddManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "manufacture/newManufacturer";
    }

    @PostMapping("/add-manufacturer")
    public String createManufacturer(@ModelAttribute(name = "manufacturer") Manufacturer manufacturer){
        manufacturerService.createManufacturer(manufacturer);
        return "redirect:/manufacturers";
    }

}
