package com.projectsky.synthetichumancorestarter.config;

import com.projectsky.synthetichumancorestarter.audit.enums.AuditMode;
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
        /**
         * max queue capacity
         * */
        private int queueCapacity = 30;
        /**
         * min thread count in thread pool
         * */
        private int corePoolSize = 1;
        /**
         * max thread count in thread pool
         * */
        private int maxPoolSize = 2;
        /**
         * keep-alive thread live time (seconds)
         * */
        private Long keepAliveSeconds = 60L;
    }

    @Data
    public static class AuditProperties {
        /**
         * changing audit mode
         * @code {console} - logging into console
         * @code {kafka} - send info in topic
         * */
        private AuditMode mode = AuditMode.CONSOLE;
        /**
         * setting kafka topic for sending audit
         * */
        private String topic = "audit-topic";
        private KafkaProperties kafka = new KafkaProperties();
    }

    @Data
    public static class KafkaProperties {
        /**
         * bootstrap servers for kafka
         * */
        private String bootstrapServers = "localhost:9092";
        /**
         * acknowledge value in kafka
         * 0 - at most once
         * 1 - at least once
         * all - at least once on all replicas
         * */
        private String acks = "1";
    }

}
