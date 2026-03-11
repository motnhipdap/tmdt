package com.dev.dungcony.modules.users.repositories;

import com.dev.dungcony.modules.users.entities.Address;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Modifying
    @Query("delete from Address a where a.id = :id")
    int deleteByIdReturnCount(@Param("id") int id);

    List<Address> findByUserId(UUID userId);
}
