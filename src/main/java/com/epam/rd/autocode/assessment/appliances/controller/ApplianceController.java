package com.epam.rd.autocode.assessment.appliances.controller;


import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/appliances")
@RequiredArgsConstructor
public class ApplianceController {

    private final ApplianceService applianceService;
    private final ManufacturerService manufacturerService;

    @GetMapping
    public String listAppliances(
            Model model,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 5, sort = {"id", "name", "category", "model", "manufacturer", "powerType", "power", "price"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ApplianceDTO> appliancesPage;
        if (search != null && !search.trim().isEmpty())
            appliancesPage = applianceService.searchAppliances(search, pageable);
        else
            appliancesPage = applianceService.getAppliances(pageable);
        model.addAttribute("appliancesPage", appliancesPage);
        model.addAttribute("search", search);
        return "appliance/appliances";
    }

    @GetMapping("/add")
    public String showAddApplianceForm(Model model) {
        model.addAttribute("categories", Category.values());
        model.addAttribute("powerTypes", PowerType.values());
        model.addAttribute("manufacturers", manufacturerService.getManufacturers());
        model.addAttribute("appliance", new Appliance());
        return "appliance/newAppliance";
    }

    @PostMapping("/add-appliance")
    public String addAppliance(Appliance appliance) {
        applianceService.createAppliance(appliance);
        return "redirect:/appliances";
    }



}

