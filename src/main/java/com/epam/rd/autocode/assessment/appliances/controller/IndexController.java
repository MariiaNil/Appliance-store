package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.config.SecurityConfig;
import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final ClientService clientService;
    private final AuthenticationManager authenticationManager;


    @Loggable
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

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Пожалуйста, введите корректные данные");
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginClient(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Model model) {
        try {
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("client", new Client());
        return "register";
    }

    @PostMapping("/register")
    public String registerAdd(@Valid @ModelAttribute("client") ClientDTO clientDTO,
                              BindingResult bindingResult,  @RequestParam("confirmPassword") String confirmPassword,
                              Model model) {
        if (!clientDTO.password().equals(confirmPassword))
            bindingResult.rejectValue("password", "error.client", "Пароли не совпадают");
        if (bindingResult.hasErrors())
            return "register";

        Client client = new Client();
        client.setName(clientDTO.name());
        client.setEmail(clientDTO.email());
        client.setPassword(clientDTO.password());
        client.setCard(clientDTO.card());
        clientService.createClient(client);
        return "redirect:/login";
    }
}
