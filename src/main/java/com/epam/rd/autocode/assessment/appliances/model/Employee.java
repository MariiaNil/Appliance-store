package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
public class Employee extends User{
    @NotBlank
    @Column(name = "department", nullable = false)
    private String department;

    public Employee(Long id, String name, String email, String password, String department) {
        super(id, name, email, password);
        this.department = department;
    }


}
