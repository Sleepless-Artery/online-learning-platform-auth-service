package org.sleepless_artery.auth_service.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.sleepless_artery.auth_service.config.kafka.properties.KafkaTopicConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaTopicConfigProperties topicConfigProperties;


    @Bean
    public NewTopic checkEmailTopic() {
        return createTopic("check-email");
    }

    @Bean
    public NewTopic changeEmailAddressTopic() {
        return createTopic("change-email-address");
    }

    @Bean
    public NewTopic resetPasswordTopic() {
        return createTopic("reset-password");
    }

    @Bean
    public NewTopic emailChangedTopic() {
        return createTopic("email-changed");
    }


    private NewTopic createTopic(String suffix) {
        return TopicBuilder
                .name(String.format("%s.%s.%s",
                        topicConfigProperties.getPrefix(), topicConfigProperties.getDomain(), suffix)
                )
                .partitions(topicConfigProperties.getPartitions())
                .replicas(topicConfigProperties.getReplicas())
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG,
                        topicConfigProperties.getMinInsyncReplicas().toString()
                )
                .build();
    }
}
