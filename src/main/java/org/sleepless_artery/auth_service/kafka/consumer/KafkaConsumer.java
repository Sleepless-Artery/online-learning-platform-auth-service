package org.sleepless_artery.auth_service.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.sleepless_artery.auth_service.service.CredentialService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CredentialService credentialService;

    @KafkaListener(topics = "user.profiles.deleted", groupId = "auth-service")
    public void listenUserDeletedEvent(String message) {
        credentialService.deleteByEmailAddress(message);
    }
}