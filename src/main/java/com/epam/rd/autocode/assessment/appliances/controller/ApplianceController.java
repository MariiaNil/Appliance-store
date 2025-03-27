package com.epam.rd.autocode.assessment.appliances.controller;


import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/appliances")
@RequiredArgsConstructor
public class ApplianceController {

    private final ApplianceService applianceService;
    private final ManufacturerService manufacturerService;

    @GetMapping
    public String listAppliances(Model model) {
        List<ApplianceDTO> applianceList = applianceService.getAppliances();
        model.addAttribute("appliances", applianceList);
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

