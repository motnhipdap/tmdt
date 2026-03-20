package com.dev.dungcony.modules.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.dungcony.modules.products.entities.Size;

public interface SizeRepository extends JpaRepository<Size, Integer> {

}
