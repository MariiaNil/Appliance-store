package com.epam.rd.autocode.assessment.appliances.password;

import com.epam.rd.autocode.assessment.appliances.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    /*@OneToOne
    @JoinColumn(name = "user_id")
    private User user;*/

    private Instant expiryDate;
}
