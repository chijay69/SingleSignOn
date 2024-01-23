package com.example.SingleSignOn.repository;

import com.example.SingleSignOn.models.Role;
import com.example.SingleSignOn.models.User;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByRole(Role userRole);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(Long id);
}
