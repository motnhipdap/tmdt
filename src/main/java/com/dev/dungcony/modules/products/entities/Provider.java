package com.dev.dungcony.modules.products.entities;

import com.dev.dungcony.modules.products.enums.ProviderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_providers")
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 10)
    @NotNull
    @Column(name = "provider_code", nullable = false, length = 10)
    private String providerCode;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ProviderStatus status = ProviderStatus.ACTIVE;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "created_at")
    private Instant createAt;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "updated_at")
    private Instant updateAt;

    @Size(max = 255)
    @Column(name = "logo")
    private String logo;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}