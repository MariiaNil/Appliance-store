package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @GetMapping
    public String listManufacturer(Model model) {
        List<Manufacturer> manufacturerList = manufacturerService.getManufacturers();
        model.addAttribute("manufacturers", manufacturerList);
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
