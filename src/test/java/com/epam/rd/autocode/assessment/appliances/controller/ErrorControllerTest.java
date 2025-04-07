package com.epam.rd.autocode.assessment.appliances.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ErrorControllerTest {

    @Test
    void testHandleErrorWithParams() {
        ErrorController controller = new ErrorController();
        Model model = mock(Model.class);
        String view = controller.handleError(404, "Not Found", model);
        verify(model).addAttribute("errorCode", 404);
        verify(model).addAttribute("errorMessage", "Not Found");
        assertEquals("errors/error", view);
    }

    @Test
    void testHandleErrorWithoutParams() {
        ErrorController controller = new ErrorController();
        Model model = mock(Model.class);
        String view = controller.handleError(null, null, model);
        verify(model).addAttribute("errorCode", 500);
        verify(model).addAttribute("errorMessage", "Произошла ошибка");
        assertEquals("errors/error", view);
    }

    @Test
    void testError403() {
        ErrorController controller = new ErrorController();
        Model model = mock(Model.class);
        String view = controller.error403("Access Denied", model);
        verify(model).addAttribute("errorCode", 403);
        verify(model).addAttribute("errorMessage", "Access Denied");
        assertEquals("errors/error403", view);
    }

    @Test
    void testError401() {
        ErrorController controller = new ErrorController();
        Model model = mock(Model.class);
        String view = controller.error401(null, model);
        verify(model).addAttribute("errorCode", 401);
        verify(model).addAttribute("errorMessage", "Для доступа к данной странице, пожалуйста, авторизуйтесь");
        assertEquals("errors/error401", view);
    }
}
