package com.dev.dungcony.modules.users.repositories;

import com.dev.dungcony.modules.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {


    Optional<User> findByLastName(String lname);

    Optional<User> findByFirstName(String fname);

    Optional<User> findByAccountId(Integer accountId);

    @Query("""
            SELECT u FROM User u
            WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))
               OR LOWER(CONCAT(u.lastName, ' ', u.firstName)) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Optional<User> findByName(@Param("name") String name);
}
