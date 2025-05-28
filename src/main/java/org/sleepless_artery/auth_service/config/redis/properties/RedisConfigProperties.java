package org.sleepless_artery.auth_service.config.redis.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Getter @Setter
@Validated
public class RedisConfigProperties {

    @NotBlank(message = "Hostname must be defined")
    private String host;

    @NotNull(message = "Port must be defined")
    private int port;
}
