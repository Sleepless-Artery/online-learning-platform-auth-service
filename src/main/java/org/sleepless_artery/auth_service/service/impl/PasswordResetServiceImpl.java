package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.dto.PasswordResetDto;
import org.sleepless_artery.auth_service.exception.BadCredentialException;
import org.sleepless_artery.auth_service.exception.CredentialNotFoundException;
import org.sleepless_artery.auth_service.model.Credential;
import org.sleepless_artery.auth_service.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final CredentialService credentialService;
    private final SendingEmailConfirmationService sendingEmailConfirmationService;
    private final VerificationService verificationService;

    private final PasswordEncoder passwordEncoder;
    private final AuthCacheService authCacheService;


    @Override
    public void initiatePasswordReset(String emailAddress) {
        log.info("Starting password reset process");

        if (!credentialService.existsByEmailAddress(emailAddress)) {
            log.warn("Cannot find credential with email address '{}'", emailAddress);
            throw new CredentialNotFoundException();
        }
        sendingEmailConfirmationService.sendEmailConfirmation(emailAddress, "reset-password");
    }


    @Override
    public boolean validatedResetCode(String emailAddress, String resetCode) {
        log.info("Validating reset code for email address '{}'", emailAddress);

        if (!credentialService.existsByEmailAddress(emailAddress)) {
            log.warn("Cannot find credential with email address '{}'", emailAddress);
            throw new CredentialNotFoundException();
        }
        return verificationService.verifyAndDeleteCode(emailAddress, resetCode);
    }


    @Override
    @Transactional
    public void completePasswordReset(PasswordResetDto passwordResetDto) {
        log.info("Completing password reset process");

        if (!passwordResetDto.getPassword().equals(passwordResetDto.getConfirmationPassword())) {
            log.warn("Password mismatch for email: {}", passwordResetDto.getEmailAddress());
            throw new BadCredentialException("Passwords do not match");
        }

        Credential credential = credentialService.findCredentialByEmailAddress(passwordResetDto.getEmailAddress());

        if (credential == null) {
            log.warn("Cannot find credential with email address '{}'", passwordResetDto.getEmailAddress());
            throw new CredentialNotFoundException();
        }

        if (passwordEncoder.matches(passwordResetDto.getPassword(), credential.getPasswordHash())) {
            log.info("Password matches old password");
            return;
        }

        credential.setPasswordHash(passwordEncoder.encode(passwordResetDto.getPassword()));
        credentialService.save(credential);

        authCacheService.evictUserCache(credential.getEmailAddress());
    }
}
