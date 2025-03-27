package com.epam.rd.autocode.assessment.appliances.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientDto {
    private Long id;
    @NotBlank(message = "{validation.client.name.notblank}")
    private String name;
    @Email(message = "{validation.client.email.invalid}")
    private String email;
    @NotBlank(message = "{validation.client.password.notblank}")
    private String password;
    @Size(min = 10, max = 20, message = "{validation.client.card.size}")
    private String card;
}
