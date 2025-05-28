package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.exception.ConfirmationException;
import org.sleepless_artery.auth_service.exception.CredentialAlreadyExistsException;
import org.sleepless_artery.auth_service.service.CredentialService;
import org.sleepless_artery.auth_service.service.EmailReservationService;
import org.sleepless_artery.auth_service.service.VerificationService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailReservationServiceImpl implements EmailReservationService {

    private final CredentialService credentialService;
    private final VerificationService verificationService;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public boolean isEmailAddressAvailable(String emailAddress) {
        log.info("Checking if email address '{}' available", emailAddress);

        return emailAddress != null
                && redisTemplate.opsForValue().get("email_reservation:" + emailAddress) == null
                && !credentialService.existsByEmailAddress(emailAddress);
    }


    @Override
    public void reserveEmailAddress(String emailAddress, Object reservationData, Duration reservationDuration) {

        log.info("Reserving email address '{}'", emailAddress);

        Boolean reserved = redisTemplate.opsForValue().setIfAbsent(
                "email_reservation:" + emailAddress,
                reservationData,
                Duration.ofMinutes(60)
        );

        if (Boolean.FALSE.equals(reserved)) {
            log.warn("Account with email address '{}' is awaiting confirmation", emailAddress);
            throw new CredentialAlreadyExistsException("Email address is already reserved");
        }

        if (credentialService.existsByEmailAddress(emailAddress)) {
            log.warn("Credential already exists with email address: {}", emailAddress);
            redisTemplate.delete("email_reservation:" + emailAddress);
            throw new CredentialAlreadyExistsException("Email address already exists");
        }
    }


    @Override
    public void checkReservation(String emailAddress, String confirmationCode) throws ConfirmationException {

        if (!redisTemplate.hasKey("email_reservation:" + emailAddress)) {
            log.warn("Email reservation does not exist for {}", emailAddress);
            throw new ConfirmationException("Email was not reserved for change");
        }

        if (!verificationService.verifyAndDeleteCode(emailAddress, confirmationCode)) {
            log.warn("Verification failed for {}", emailAddress);
            throw new ConfirmationException("Wrong confirmation code");
        }
    }
}
