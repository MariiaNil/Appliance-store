package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.config.SecurityConfig;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.password.LoginAttemptService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final ClientService clientService;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;


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

    /*@PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            HttpServletRequest request) {

        // Проверка блокировки
        if (loginAttemptService.isBlocked(email)) {
            model.addAttribute("error", "Аккаунт заблокирован! Попробуйте позже.");
            return "login";
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            loginAttemptService.loginSuccess(email);
            return "redirect:/";

        } catch (AuthenticationException e) {
            loginAttemptService.loginFailed(email); // Фиксируем попытку

            // Формируем сообщение
            int remaining = loginAttemptService.getRemainingAttempts(email);
            String errorMsg = "Неверные данные. Осталось попыток: " + remaining;

            model.addAttribute("error", errorMsg);
            return "login";
        }
    }*/

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
    public String registerAdd(Client client) {
        clientService.createClient(client);
        return "redirect:/login";
    }
}
