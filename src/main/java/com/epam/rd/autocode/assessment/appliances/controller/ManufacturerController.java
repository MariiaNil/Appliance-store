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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService manufacturerService;
    private static final Logger logger = LoggerFactory.getLogger(ManufacturerController.class);

    @GetMapping
    public String listManufacturer(
            Model model,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 5, sort = {"id", "name"}, direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("Fetching manufacturers list. Search query: '{}', Page size: {}, Sort: {}, Direction: {}",
                search, pageable.getPageSize(), pageable.getSort(), pageable.getSort().iterator().next().getDirection());

        Page<ManufacturerDTO> manufacturerPage;
        if (search != null && !search.trim().isEmpty()) {
            manufacturerPage = manufacturerService.searchManufacturers(search, pageable);
            logger.info("Search mode enabled. Found {} manufacturers for query '{}'", manufacturerPage.getTotalElements(), search);
        } else {
            manufacturerPage = manufacturerService.getAllManufacturers(pageable);
            logger.info("Fetching all manufacturers. Total manufacturers found: {}", manufacturerPage.getTotalElements());
        }
        model.addAttribute("manufacturerPage", manufacturerPage);
        model.addAttribute("search", search);
        logger.info("Manufacturers list successfully loaded, returning view 'manufacture/manufacturers'");
        return "manufacture/manufacturers";
    }

    @GetMapping("/add")
    public String showAddManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        logger.info("Showing add manufacturer form");
        return "manufacture/newManufacturer";
    }

    @PostMapping("/add-manufacturer")
    public String createManufacturer(@ModelAttribute(name = "manufacturer") Manufacturer manufacturer){
        manufacturerService.createManufacturer(manufacturer);
        logger.info("Manufacturer created successfully");
        return "redirect:/manufacturers";
    }

}
