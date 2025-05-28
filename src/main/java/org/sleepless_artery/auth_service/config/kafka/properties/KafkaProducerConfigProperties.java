package org.sleepless_artery.auth_service.config.kafka.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


@Configuration
@ConfigurationProperties(prefix = "spring.kafka.producer")
@Getter @Setter
@Validated
public class KafkaProducerConfigProperties {

    @NotBlank(message = "Bootstrap servers must be defined")
    private String bootstrapServers;

    @NotBlank(message = "Key serializer must be defined")
    private String keySerializer;

    @NotBlank(message = "Value serializer must be defined")
    private String valueSerializer;

    @NotNull(message = "Number of retries must be defined")
    private Integer retries;

    @Pattern(regexp = "all|1|0", message = "Incorrect value for this property")
    private String acks;

    @NotNull(message = "Batch size must be defined")
    private Integer batchSize;

    @NotNull(message = "Delivery timeout must be defined")
    private Integer deliveryTimeoutMs;

    @NotNull(message = "Value of linger must be defined")
    private Integer lingerMs;

    @NotNull(message = "Request timeout must be defined")
    private Integer requestTimeoutMs;

    @NotNull(message = "Number of retries backoff must be defined")
    private Integer retryBackoffMs;
}
