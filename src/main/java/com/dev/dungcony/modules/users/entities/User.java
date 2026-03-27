package com.dev.dungcony.modules.users.entities;

import com.dev.dungcony.commons.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tbl_users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @NotBlank(message = "firstName not blank")
    @Column(name = "f_name", nullable = false)
    private String firstName;

    @NotBlank(message = "lastName not blank")
    @Column(name = "l_name", nullable = false)
    private String lastName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "acc_id")
    private Integer accountId;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Receiver> receivers = new ArrayList<>();
}
