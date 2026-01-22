package com.dev.dungcony.modules.authorization.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.dungcony.modules.authorization.entities.Account;

@Repository
public interface AccRepository extends JpaRepository<Account, Integer> {

    /**
     * Tìm account theo email
     */
    Optional<Account> findByEmail(String email);

    /**
     * Tìm account theo username
     */
    Optional<Account> findByUsername(String username);

    /**
     * Tìm account theo phone
     */
    Optional<Account> findByPhone(String phone);

    /**
     * Kiểm tra email đã tồn tại
     */
    boolean existsByEmail(String email);

    /**
     * Kiểm tra username đã tồn tại
     */
    boolean existsByUsername(String username);

    /**
     * Kiểm tra phone đã tồn tại
     */
    boolean existsByPhone(String phone);
}
