package com.ninjas.gig.repository;

import com.ninjas.gig.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    Boolean existsByUsername(String username);
}
