package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeDTOMapper implements Function<Employee, EmployeeDTO> {

    @Override
    public EmployeeDTO apply(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPassword(),
                employee.getDepartment()
        );
    }
}

// реализовать страницу ордер +
// реализовать нормальный маппер +
    //дто +
// пагинатор, сортировка, поиск + // ДоДЕЛАТЬ И ОРДЕРА +
// секурити
    // Хешширование паролей + (захешировать по другому!)
    // по ролям +
    // вход с помощью гугла, фейсбука
    // логги
// завести локализацию +
// логирование + (через аспект)
// тесты
    // для контролеров +
    // для сервисов
// ExceptionsHENDLER
    // логги
// Дата валидацию +
