package com.epam.rd.autocode.assessment.appliances.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/errors")
public class ErrorController {

    @GetMapping
    public String handleError(@RequestParam(required = false) Integer code,
                              @RequestParam(required = false) String message,
                              Model model) {
        model.addAttribute("errorCode", code != null ? code : 500);
        model.addAttribute("errorMessage", message != null ? message : "Произошла ошибка");
        return "errors/error";
    }

    @GetMapping("/403")
    public String error403(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("errorCode", 403);
        model.addAttribute("errorMessage", message != null ? message : "У вас недостаточно прав для доступа к этой странице");
        return "errors/error403";
    }

    @GetMapping("/401")
    public String error401(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("errorCode", 401);
        model.addAttribute("errorMessage", message != null ? message : "Для доступа к данной странице, пожалуйста, авторизуйтесь");
        return "errors/error401";
    }
}
