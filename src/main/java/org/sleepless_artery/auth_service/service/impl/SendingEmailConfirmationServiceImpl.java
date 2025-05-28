package org.sleepless_artery.auth_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.kafka.producer.KafkaProducer;
import org.sleepless_artery.auth_service.service.SendingEmailConfirmationService;
import org.sleepless_artery.auth_service.service.VerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SendingEmailConfirmationServiceImpl implements SendingEmailConfirmationService {

    private final VerificationService verificationService;
    private final KafkaProducer kafkaProducer;

    @Value("${spring.kafka.topic.prefix}")
    private String prefix;

    @Value("${spring.kafka.topic.domain}")
    private String domain;


    @Override
    public void sendEmailConfirmation(String emailAddress, String postfix) {
        String confirmationCode = verificationService.saveVerificationCode(emailAddress);

        kafkaProducer.send(
                String.format("%s.%s.%s", prefix, domain, postfix),
                emailAddress,
                confirmationCode
        );

        log.info("Confirmation code sent to email address '{}'", emailAddress);
    }
}
