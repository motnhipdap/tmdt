package com.dev.dungcony.modules.users.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tbl_users")
public class User {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @NotBlank(message = "name not blank")
    @Column(name = "f_name", nullable = false)
    private String firstName;

    @NotBlank(message = "name not blank")
    @Column(name = "l_name", nullable = false)
    private String lastName;

    @Column(name = "img")
    private String img;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "acc_id")
    private int accId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
