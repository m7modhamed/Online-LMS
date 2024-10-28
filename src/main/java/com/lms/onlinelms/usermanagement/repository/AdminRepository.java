package com.lms.onlinelms.usermanagement.repository;

import com.lms.onlinelms.usermanagement.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String mail);
}
