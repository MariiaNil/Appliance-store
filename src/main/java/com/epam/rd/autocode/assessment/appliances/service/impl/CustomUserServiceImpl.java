package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.CustomUserService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
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
    @Loggable
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Client> clientOpt = clientRepository.findByEmail(username);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            return new UserDetailsImpl(
                    client.getId(),
                    client.getEmail(),
                    client.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_CLIENT"),
                    client.getCard(),
                    null
            );
        }
        Optional<Employee> employeeOpt = employeeRepository.findByEmail(username);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            return new UserDetailsImpl(
                    employee.getId(),
                    employee.getEmail(),
                    employee.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_EMPLOYEE"),
                    null,
                    employee.getDepartment()
            );
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}


/*@Override
@Loggable
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<Client> clientOpt = clientRepository.findByEmail(username);
    if (clientOpt.isPresent()) {
        Client client = clientOpt.get();
        return new UserDetailsImpl(
                client.getId(),
                client.getEmail(),
                client.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_CLIENT"),
                client.getCard(),
                null
        );
    }
    Optional<Employee> employeeOpt = employeeRepository.findByEmail(username);
    if (employeeOpt.isPresent()) {
        Employee employee = employeeOpt.get();
        return new UserDetailsImpl(
                employee.getId(),
                employee.getEmail(),
                employee.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_EMPLOYEE"),
                null,
                employee.getDepartment()
        );
    } else {
        throw new UsernameNotFoundException("User not found");
    }
}*/
