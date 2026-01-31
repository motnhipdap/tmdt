package com.dev.dungcony.modules.users.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity(name = "tbl_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User u;

    @NotBlank(message = "country not not")
    @Column(name = "country", nullable = false, length = 20)
    private String country;

    @NotBlank(message = "province not not")
    @Column(name = "province", nullable = false, length = 20)
    private String province;

    @NotBlank(message = "district not not")
    @Column(name = "district", nullable = false, length = 20)
    private String district;

    @NotBlank(message = "street not not")
    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "detail", length = 255)
    private String detail;
}
