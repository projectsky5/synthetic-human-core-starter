package com.projectsky.synthetichumancorestarter.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "synthetic.human.core")
public class SyntheticProperties {

    private boolean enabled = true;

    private CommandProperties command = new CommandProperties();
    private AuditProperties audit = new AuditProperties();

    @Data
    public static class CommandProperties {
        private int queueCapacity = 30;
        private int corePoolSize = 1;
        private int maxPoolSize = 2;
        private Long keepAliveSeconds = 60L;
    }

    @Data
    public static class AuditProperties {
        private String mode = "console";
        private String topic = "audit-topic";
        private KafkaProperties kafka = new KafkaProperties();
    }

    @Data
    public static class KafkaProperties {
        private String bootstrapServers = "localhost:9092";
        private String acks = "1";
    }

}
