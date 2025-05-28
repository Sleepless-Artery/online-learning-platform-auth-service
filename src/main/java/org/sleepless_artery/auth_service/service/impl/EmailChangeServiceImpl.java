package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.dto.CredentialDto;
import org.sleepless_artery.auth_service.exception.BadCredentialException;
import org.sleepless_artery.auth_service.exception.CredentialAlreadyExistsException;
import org.sleepless_artery.auth_service.kafka.producer.KafkaProducer;
import org.sleepless_artery.auth_service.model.Credential;
import org.sleepless_artery.auth_service.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailChangeServiceImpl implements EmailChangeService {

    private final CredentialService credentialService;
    private final SendingEmailConfirmationService sendingEmailConfirmationService;
    private final EmailReservationService emailReservationService;

    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaProducer kafkaProducer;

    @Value("${spring.kafka.topic.prefix}")
    private String prefix;

    @Value("${spring.kafka.topic.domain}")
    private String domain;


    @Override
    @Transactional
    public void changeEmailAddress(CredentialDto credentialDto, String newEmailAddress) {
        log.info("Changing email address from '{}' to '{}'", credentialDto.getEmailAddress(), newEmailAddress);

        Credential credential = credentialService.findCredentialByEmailAddress(credentialDto.getEmailAddress());

        if (credentialDto.getPassword() == null ||
                !passwordEncoder.matches(credentialDto.getPassword(), credential.getPasswordHash())
        ) {
            log.error("Passwords do not match");
            throw new BadCredentialException("Passwords do not match");
        }

        emailReservationService.reserveEmailAddress(
                newEmailAddress,
                credentialDto,
                Duration.ofMinutes(60)
        );
        sendingEmailConfirmationService.sendEmailConfirmation(newEmailAddress, "change-email-address");
    }


    @Override
    @Transactional
    public void confirmEmailAddress(String oldEmailAddress, String newEmailAddress, String confirmationCode) {
        log.info("Confirming new email address");

        emailReservationService.checkReservation(newEmailAddress, confirmationCode);

        try {
            credentialService.changeEmailAddress(oldEmailAddress, newEmailAddress);
            log.info("Email changed successfully from {} to {}", oldEmailAddress, newEmailAddress);
        } catch (DataIntegrityViolationException e) {
            log.error("Email change conflict for {}", newEmailAddress, e);
            throw new CredentialAlreadyExistsException("Email already exists");
        } finally {
            redisTemplate.delete("email_reservation:" + newEmailAddress);
            redisTemplate.delete("email_reservation:" + oldEmailAddress);
        }

        kafkaProducer.send(
                String.format("%s.%s.%s", prefix, domain, "email-changed"), oldEmailAddress, newEmailAddress
        );
    }
}