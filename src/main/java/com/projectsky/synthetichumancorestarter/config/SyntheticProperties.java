package com.projectsky.synthetichumancorestarter.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "synthetic.human.core")
public class SyntheticProperties {

    private boolean enabled;

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
        private int keepAliveSeconds = 60;
    }

    @Data
    public static class AuditProperties {
        /**
         * changing audit mode
         * @code {console} - logging into console
         * @code {kafka} - send info in topic
         * */
        private String mode;
        /**
         * setting kafka topic for sending audit
         * */

        private KafkaProperties kafka = new KafkaProperties();
    }

    @Data
    public static class KafkaProperties {
        private String topic = "audit-topic";
    }

}
