package com.epam.rd.autocode.assessment.appliances.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private IndexController controller;

    @Test
    void testIndex() {
        Locale testLocale = new Locale("en");
        LocaleContextHolder.setLocale(testLocale);

        String view = controller.index(model);

        verify(model).addAttribute("currentLanguage", testLocale.getLanguage());
        assertEquals("index", view);
    }

    @Test
    void testChangeLanguage() {
        String lang = "fr";
        Locale newLocale = new Locale(lang);

        String view = controller.changeLanguage(lang, request);

        // Проверяем, что view корректный и локаль изменена
        assertEquals("redirect:/", view);
        assertEquals(newLocale, LocaleContextHolder.getLocale());
    }
}
