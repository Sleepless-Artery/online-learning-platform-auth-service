package org.sleepless_artery.auth_service.config.kafka.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


@Configuration
@ConfigurationProperties(prefix = "spring.kafka.topic")
@Getter @Setter
@Validated
public class KafkaTopicConfigProperties {

    @NotBlank(message = "Prefix must be defined")
    private String prefix;

    @NotBlank(message = "Domain must be defined")
    private String domain;

    @NotNull(message = "Number of partitions must be defined")
    @Min(value = 1, message = "Number of partitions cannot be less than 1")
    private Integer partitions;

    @NotNull(message = "Number of replicas must be defined")
    @Min(value = 1, message = "Number of replicas cannot be less than 1")
    private Short replicas;

    @NotNull(message = "Minimum number of replicas that must confirm the record must be defined")
    @Min(value = 1, message = "Number of confirming replicas cannot be less than 1")
    private Integer minInsyncReplicas;
}
