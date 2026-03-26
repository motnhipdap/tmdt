package com.dev.dungcony.modules.order.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    @Column(name = "address_id", nullable = false)
    private Integer addressId;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String receiverName;

    @Size(max = 10)
    @Column(name = "phone", nullable = false)
    private String phone;


}
