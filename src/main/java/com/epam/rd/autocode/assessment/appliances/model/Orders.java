package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Set<OrderRow> orderRowSet = new HashSet<>();
    private Boolean approved;

}
