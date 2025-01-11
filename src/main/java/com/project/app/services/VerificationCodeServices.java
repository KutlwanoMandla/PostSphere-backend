package com.project.app.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.app.entities.VerificationCode;
import com.project.app.repositories.VerificationCodeRepo;

@Service
public class VerificationCodeServices {
    
    @Autowired
    private VerificationCodeRepo verificationCodeRepo;

    @Transactional
    public String generateCode(String email) {

        String code = String.format("%06d", new Random().nextInt(999999));

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        // Save the code in the database
        VerificationCode verificationCode = new VerificationCode(email, code, expiresAt);
        // always delete the existing code associated with the email adress.
        verificationCodeRepo.deleteByEmail(email);
        verificationCodeRepo.save(verificationCode);

        return code;
    }

    @Transactional(readOnly = true)
    public boolean validateCode(String email, String code) {
        Optional<VerificationCode> optionalCode = verificationCodeRepo.findByEmailAndCode(email, code);
        if (optionalCode.isPresent()) {
            VerificationCode verificationCode = optionalCode.get();
            // check if the code is expired
            if (verificationCode.getExpiresAt().isAfter(LocalDateTime.now())) {
                return true;
            }
        }
        
        return false;
    }
}
