package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.entities.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {
    Optional<Provider> findByCode(String code);

    Optional<Provider> findByName(String name);

}
