package com.epam.rd.autocode.assessment.appliances.controller;


import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.CategoryDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.CategoryService;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/appliances")
@RequiredArgsConstructor
public class ApplianceController {


    private final ApplianceService applianceService;
    private final ManufacturerService manufacturerService;
    private final CategoryService categoryService;
    private final String uploadDir = "./uploads/images/appliances/";

    @GetMapping
    public String listAppliances(
            Model model,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 8, sort = {"id", "name", "category", "model", "manufacturer", "powerType", "power", "price"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ApplianceDTO> appliancesPage;
        if (categoryId != null) {
            appliancesPage = applianceService.getByCategory(categoryId, pageable);
            model.addAttribute("selectedCategoryId", categoryId);
        }
        else if (search != null && !search.trim().isEmpty())
            appliancesPage = applianceService.searchAppliances(search, pageable);
        else
            appliancesPage = applianceService.getAppliances(pageable);

        List<CategoryDTO> allCats = categoryService
                .getAllCategories(PageRequest.of(0, 10))
                .getContent();

        model.addAttribute("appliancesPage", appliancesPage);
        model.addAttribute("search", search);
        model.addAttribute("categories", allCats);
        return "appliance/appliances";
    }

    @GetMapping("/add")
    public String showAddApplianceForm(Model model,
                                       @RequestParam(value = "size", defaultValue = "100") int size) {

        Pageable pageable = PageRequest.of(0, size, Sort.by("id").ascending());
        List<ManufacturerDTO> manufacturerDTO = manufacturerService.getAllManufacturers(pageable).getContent();
        List<CategoryDTO> categoriesDTO = categoryService
                .getAllCategories(PageRequest.of(0, size, Sort.by("name")))
                .getContent();
        model.addAttribute("categories", categoriesDTO);
        model.addAttribute("powerTypes", PowerType.values());
        model.addAttribute("manufacturers", manufacturerDTO);
        model.addAttribute("appliance", new Appliance());
        return "appliance/newAppliance";
    }


    @PostMapping("/add-appliance")
    public String addAppliance(@ModelAttribute Appliance appliance,
                               @RequestParam("categoryId")   Long categoryId,
                               @RequestParam("manufacturerId") Long manufacturerId,
                               @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        categoryService.getById(categoryId).ifPresent(appliance::setCategory);
        manufacturerService.getById(manufacturerId).ifPresent(appliance::setManufacturer);

        Appliance saved = applianceService.createAppliance(appliance);

        if (!imageFile.isEmpty()) {
            String original = imageFile.getOriginalFilename();
            String ext = original.contains(".")
                    ? original.substring(original.lastIndexOf('.') + 1)
                    : "jpg";
            String filename = saved.getId() + "." + ext;
            Path uploadPath = Paths.get(uploadDir);
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream in = imageFile.getInputStream()) {
                Files.copy(in, uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            }
            saved.setImageUrl("/images/appliances/" + filename);
            applianceService.createAppliance(saved);
        }

        return "redirect:/appliances";
    }


    @GetMapping("/{id}")
    public String showApplianceDetail(@PathVariable Long id, Model model) {
        ApplianceDTO appliance = applianceService.getApplianeById(id);
        model.addAttribute("appliance", appliance);
        return "appliance/applianceDetail";
    }

    /*
    * Сделать сортировку по производителю на странице еплаинс
    * Реализовать окно просмотра товара
    * Переделать дмл для аплаинс
    * Сделать отображение цены в грн
    * Переделать заглавную страницу (добавить категории, и товары)
    * Поправить футтер
    * Дополнительно: (оптимизировать бд для большой информации)
    *                поправить поиск по категории*/


    /*@PostMapping("/add-appliance")
    public String addAppliance(
            @ModelAttribute Appliance appliance,
            @RequestParam("categoryId")   Long categoryId,
            @RequestParam("manufacturerId") Long manufacturerId,
            @RequestParam("imageFile")    MultipartFile imageFile
    ) {
        String filename = StringUtils.cleanPath(imageFile.getOriginalFilename());
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream in = imageFile.getInputStream()) {
                Files.copy(in,
                        uploadPath.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to save appliance image", ex);
        }
        appliance.setImageUrl("/images/appliances/" + filename);
        categoryService.getById(categoryId).ifPresent(appliance::setCategory);
        manufacturerService.getById(manufacturerId).ifPresent(appliance::setManufacturer);
        applianceService.createAppliance(appliance);
        return "redirect:/appliances";
    }*/

    /*@PostMapping("/add-appliance")
    public String addAppliance(@ModelAttribute Appliance appliance) {
        applianceService.createAppliance(appliance);
        return "redirect:/appliances";
    }*/

}

