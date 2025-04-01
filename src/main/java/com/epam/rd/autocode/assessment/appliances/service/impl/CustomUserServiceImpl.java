package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.CustomUserService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements CustomUserService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Client> client = clientRepository.findByEmail(username);
        if (client.isPresent()) {
            Client c = client.get();
            return User.withUsername(c.getEmail())
                    .password(c.getPassword())
                    .roles("CLIENT")
                    .build();
        }
        Optional<Employee> employee = employeeRepository.findByEmail(username);
        if (employee.isPresent()) {
            Employee e = employee.get();
            return User.withUsername(e.getEmail())
                    .password(e.getPassword())
                    .roles("EMPLOYEE")
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
