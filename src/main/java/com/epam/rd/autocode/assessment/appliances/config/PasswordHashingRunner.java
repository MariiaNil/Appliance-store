package com.epam.rd.autocode.assessment.appliances.config;

import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordHashingRunner {
    @Bean
    public CommandLineRunner hashPasswords(EmployeeService employeeService, ClientService clientService) {
        return args -> {
            employeeService.hashExistingPasswords();
            clientService.hashExistingPasswords();
            System.out.println("Пароли успешно захешированы!");
        };
    }
}
