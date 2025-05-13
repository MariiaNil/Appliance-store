package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    @GetMapping
    public String listCategories(Model model) {
        List<Category> categoryList = Arrays.stream(Category.values())
                .filter(c -> c != Category.BIG && c != Category.SMALL)
                .toList();
        model.addAttribute("categories", categoryList);
        return "category/categories";
    }
}
