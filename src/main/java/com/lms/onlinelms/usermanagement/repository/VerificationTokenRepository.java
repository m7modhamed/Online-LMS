package com.lms.onlinelms.usermanagement.repository;

import com.lms.onlinelms.usermanagement.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    void deleteByUserId(Long id);
}
