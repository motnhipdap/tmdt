package com.dev.dungcony.modules.users.repositories;

import com.dev.dungcony.modules.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // findBy + <FieldName> + <Condition>
    // @Query("""
    // select u from User u
    // where u.point > :point
    // and u.address like %:addr%
    // """)
    // List<User> findActiveUsers(
    // @Param("point") int point,
    // @Param("addr") String addr
    // );

    Optional<User> findByLastName(String lname);

    Optional<User> findByFirstName(String fname);

    List<User> findByImgIsNull();

    List<User> findByImgIsNotNull();

    Optional<User> findByAccountId(Integer accountId);
}
