package com.dev.dungcony.modules.users.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(name = "tbl_address")
public class Address {
    @Id
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Receiver receiver;

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
