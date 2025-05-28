package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.dto.CredentialDto;
import org.sleepless_artery.auth_service.dto.JwtResponse;
import org.sleepless_artery.auth_service.dto.RegistrationCredentialDto;
import org.sleepless_artery.auth_service.exception.BadCredentialException;
import org.sleepless_artery.auth_service.exception.CredentialAlreadyExistsException;
import org.sleepless_artery.auth_service.model.Credential;
import org.sleepless_artery.auth_service.service.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final AuthService authService;
    private final CredentialService credentialService;
    private final SendingEmailConfirmationService sendingEmailConfirmationService;
    private final EmailReservationService emailReservationService;

    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public void startRegistration(RegistrationCredentialDto credentialDto) {

        log.info("Registering user with email address '{}'", credentialDto.getEmailAddress());

        emailReservationService.reserveEmailAddress(
                credentialDto.getEmailAddress(),
                credentialDto,
                Duration.ofMinutes(60)
        );

        if (!credentialDto.getPassword().equals(credentialDto.getConfirmationPassword())) {
            log.warn("Password mismatch for email: {}", credentialDto.getEmailAddress());
            redisTemplate.delete("email_reservation:" + credentialDto.getEmailAddress());
            throw new BadCredentialException("Passwords do not match");
        }

        sendingEmailConfirmationService.sendEmailConfirmation(credentialDto.getEmailAddress(), "check-email");
    }


    @Override
    public JwtResponse confirmRegistration(RegistrationCredentialDto credentialDto, String confirmationCode) {

        log.info("Confirming registration for email address: {}", credentialDto.getEmailAddress());


        emailReservationService.checkReservation(credentialDto.getEmailAddress(), confirmationCode);

        Credential credential;
        try {
            credential = credentialService.createCredential(credentialDto);
        } catch (DataIntegrityViolationException e) {
            throw new CredentialAlreadyExistsException("Email already registered");
        } finally {
            redisTemplate.delete("email_reservation:" + credentialDto.getEmailAddress());
        }

        log.info("Successfully created credential with email address: {}", credentialDto.getEmailAddress());

        return authService.createAuthenticationToken(new CredentialDto(
                credential.getEmailAddress(),
                credentialDto.getPassword()
        ));
    }
}