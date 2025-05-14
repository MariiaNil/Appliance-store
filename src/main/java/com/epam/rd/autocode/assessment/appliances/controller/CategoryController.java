package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.CategoryDTO;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.service.CategoryService;
import java.io.IOException;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final String uploadDir = "./uploads/images/categories/";


    @GetMapping
    public String listCategories(
            Model model,
            @PageableDefault(size = 8, sort = {"id", "name"}, direction = Sort.Direction.ASC) Pageable pageable){

        Page<CategoryDTO> categoryDTOPage;
        categoryDTOPage = categoryService.getAllCategories(pageable);
        model.addAttribute("categories", categoryDTOPage.getContent());
        model.addAttribute("categoryPage", categoryDTOPage);
        return "category/categories";
    }

    @GetMapping("/add")
    public String showAddCategoriesFrom(Model model) {
        model.addAttribute("category", new Category());
        return "category/newCategory";
    }

    @PostMapping("/add-category")
    public String createCategory(@ModelAttribute(name = "category") CategoryDTO categoryDTO,
                                 @RequestParam("imageFile") MultipartFile imageFile) {

        String filename = StringUtils.cleanPath(imageFile.getOriginalFilename());
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (Files.notExists(uploadPath))
                Files.createDirectories(uploadPath);
            try (InputStream in = imageFile.getInputStream()) {
                Files.copy(in, uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/categories";
        }


        Category category = new Category();
        category.setName(categoryDTO.name());
        category.setImageUrl("/images/categories/" + filename);

        categoryService.createCategory(category);
        return "redirect:/categories";
    }



    /*@GetMapping
    public String listCategories(Model model) {
        List<Category> categoryList = Arrays.stream(Category.values())
                .filter(c -> c != Category.BIG && c != Category.SMALL)
                .toList();
        model.addAttribute("categories", categoryList);
        return "category/categories";
    }*/
}
