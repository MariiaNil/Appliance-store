package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeDTOMapper implements Function<Employee, EmployeeDTO> {

    @Override
    public EmployeeDTO apply(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPassword(),
                employee.getDepartment()
        );
    }
}

// реализовать страницу ордер
// реализовать нормальный маппер +
    //дто +
// пагинатор, сортировка, поиск + // ТОЛЬКО НЕ ОРДЕРА
// секурити
    // по ролям
    // вход с помощью гугла, фейсбука
// завести локализацию
    // завести локаль для ордера в мессенджере
// логирование
// тесты
    // для контролеров
    // для сервисов
// ExceptionsHENDLER
// Хешширование паролей