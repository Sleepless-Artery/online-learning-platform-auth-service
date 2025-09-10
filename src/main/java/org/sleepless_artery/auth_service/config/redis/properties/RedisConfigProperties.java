package org.sleepless_artery.auth_service.config.redis.properties;

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

    private String host;
    private int port;
    private String password;

    private Cluster cluster = new Cluster();


    @Getter @Setter
    public static class Cluster {
        private String nodes;
        private Integer maxRedirects;
    }

    public boolean isClusterMode() {
        return cluster != null && cluster.getNodes() != null && !cluster.getNodes().isEmpty();
    }
}
