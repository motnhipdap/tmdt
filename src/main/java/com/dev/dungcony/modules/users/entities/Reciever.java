package com.dev.dungcony.modules.users.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tbl_recivers")
public class Reciever {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "name", lenAth = 100)
    private String receiverName;

    @Size(max = 10)
    @Column(name = "phone", nullable = false)
    private String phone;


    //-----FK-----//
    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
}
