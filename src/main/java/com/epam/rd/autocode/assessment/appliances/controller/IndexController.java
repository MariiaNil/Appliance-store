package com.epam.rd.autocode.assessment.appliances.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final MessageSource messageSource;

    @GetMapping
    public String index(Model model) {
        String currentLanguage = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("currentLanguage", currentLanguage);
        return "index";
    }

    @GetMapping("/change-lang")
    public String changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request) {
        Locale locale = new Locale(lang);
        LocaleContextHolder.setLocale(locale);
        return "redirect:/";
    }
}
