package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
public class Client extends User{
    @NotBlank
    @Column(name = "card", unique = true, nullable = false)
    private String card;

    public Client(Long id, String name, String email, String password, String card) {
        super(id, name, email, password);
        this.card = card;
    }
}
