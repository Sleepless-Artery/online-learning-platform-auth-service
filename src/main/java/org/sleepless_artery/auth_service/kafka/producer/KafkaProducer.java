package org.sleepless_artery.auth_service.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String key, String value) {
        log.info("Sending message to the topic '{}'", topic);
        kafkaTemplate.send(topic, key, value);
    }
}
