package com.project.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.project.app.entities.VerificationCode;

@RepositoryRestResource
public interface VerificationCodeRepo extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmail(String email);
    Optional<VerificationCode> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email);
}
