package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.entities.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {
}
