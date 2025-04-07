package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IndexControllerTest {

    @Mock
    private ClientService clientService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Model model;

    private IndexController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new IndexController(clientService, authenticationManager);
    }

    @Test
    void testIndex() {
        // Устанавливаем язык по умолчанию через LocaleContextHolder
        LocaleContextHolder.setLocale(java.util.Locale.ENGLISH);
        String view = controller.index(model);
        verify(model).addAttribute("currentLanguage", "en");
        assertEquals("index", view);
    }

    @Test
    void testChangeLanguage() {
        // Проверяем, что при смене языка редирект происходит на главную
        String view = controller.changeLanguage("ru", mock(HttpServletRequest.class));
        assertEquals("redirect:/", view);
    }

    @Test
    void testLoginGet() {
        String view = controller.login(null, model);
        assertEquals("login", view);
        // Если ошибка не передана, никаких дополнительных атрибутов не добавляется
    }

    @Test
    void testLoginPostSuccess() throws Exception {
        // Для упрощения – проверяем, что в случае успешной аутентификации происходит редирект
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        String view = controller.loginClient("user", "pass", model);
        assertEquals("redirect:/", view);
    }

    @Test
    void testLoginPostFailure() {
        // Симулируем исключение при аутентификации
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("auth failed"));
        String view = controller.loginClient("user", "wrong", model);
        verify(model).addAttribute("error", "Неверное имя пользователя или пароль");
        assertEquals("login", view);
    }

    @Test
    void testLogout() {
        String view = controller.logout();
        assertEquals("logout", view);
    }

    @Test
    void testRegisterGet() {
        String view = controller.register(model);
        verify(model).addAttribute(eq("client"), any(Client.class));
        assertEquals("register", view);
    }

    @Test
    void testRegisterPost() {
        // Создаем DTO для регистрации
        ClientDTO clientDTO = new ClientDTO(null, "Test Name", "test@example.com", "Test@1234", "1234567890123456");
        // Создаем BindingResult для объекта clientDTO
        BindingResult bindingResult = new BeanPropertyBindingResult(clientDTO, "client");
        // Подтверждение пароля
        String confirmPassword = "Test@1234";
        Model model = new ExtendedModelMap();

        // Вызываем метод контроллера
        String view = controller.registerAdd(clientDTO, bindingResult, confirmPassword, model);

        // Проверяем, что сервис вызывается с правильной сущностью
        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientService, times(1)).createClient(captor.capture());
        Client createdClient = captor.getValue();

        // Сверяем поля
        assertEquals("Test Name", createdClient.getName());
        assertEquals("test@example.com", createdClient.getEmail());
        assertEquals("Test@1234", createdClient.getPassword());
        assertEquals("1234567890123456", createdClient.getCard());

        // Проверяем, что возвращается редирект на страницу логина
        assertEquals("redirect:/login", view);
    }
}
