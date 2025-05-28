package org.sleepless_artery.auth_service.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.sleepless_artery.auth_service.config.kafka.properties.KafkaProducerConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaProducerConfigProperties configProperties;


    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, configProperties.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, configProperties.getValueSerializer());
        props.put(ProducerConfig.ACKS_CONFIG, configProperties.getAcks());
        props.put(ProducerConfig.RETRIES_CONFIG, configProperties.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, configProperties.getBatchSize());
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, configProperties.getDeliveryTimeoutMs());
        props.put(ProducerConfig.LINGER_MS_CONFIG, configProperties.getLingerMs());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, configProperties.getRequestTimeoutMs());
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, configProperties.getRetryBackoffMs());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
