package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "appliance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "name")
    private String name;

    /*@Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;*/

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NotBlank
    @Column(name = "model")
    private String model;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @Column(name = "powerType")
    @Enumerated(EnumType.STRING)
    private PowerType powerType;

    @Lob
    @Column(name = "characteristic", columnDefinition = "CLOB")
    private String characteristic;
    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    private String description;
    @Min(1)
    @Column(name = "power")
    private Integer power;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "image_url")
    private String imageUrl;
}
