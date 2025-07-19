package com.projectsky.synthetichumancorestarter.config;

import com.projectsky.synthetichumancorestarter.audit.AuditKafkaProducer;
import com.projectsky.synthetichumancorestarter.audit.aop.ConsoleAuditAspect;
import com.projectsky.synthetichumancorestarter.audit.aop.KafkaAuditAspect;
import com.projectsky.synthetichumancorestarter.command.model.Command;
import com.projectsky.synthetichumancorestarter.command.service.CommandExecutor;
import com.projectsky.synthetichumancorestarter.command.service.CommandService;
import com.projectsky.synthetichumancorestarter.exception.SyntheticExceptionHandler;
import com.projectsky.synthetichumancorestarter.metrics.CommandMetricsPublisher;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Configuration
@EnableConfigurationProperties(SyntheticProperties.class)
@ConditionalOnProperty(prefix = "synthetic.human.core", name = "enabled", havingValue = "true")
public class SyntheticHumanAutoConfiguration {

    private final SyntheticProperties properties;
    private final KafkaProperties kafkaProperties;

    public SyntheticHumanAutoConfiguration(SyntheticProperties properties, KafkaProperties kafkaProperties) {
        this.properties = properties;
        this.kafkaProperties = kafkaProperties;
    }

    /* COMMAND MODULE */

    @Bean
    public BlockingQueue<Runnable> commandQueue(SyntheticProperties properties) {
        return new LinkedBlockingQueue<>(properties.getCommand().getQueueCapacity());
    }

    @Bean
    public ExecutorService commandExecutorService(SyntheticProperties properties, BlockingQueue<Runnable> queue){
        return new ThreadPoolExecutor(
                properties.getCommand().getCorePoolSize(),
                properties.getCommand().getMaxPoolSize(),
                properties.getCommand().getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                queue
        );
    }

    @Bean
    public CommandExecutor commandExecutor(CommandMetricsPublisher commandMetricsPublisher){
        return new CommandExecutor(commandMetricsPublisher);
    }

    @Bean
    public CommandService commandService(ExecutorService executorService, CommandExecutor executor){
        return new CommandService(executorService, executor);
    }

    /* AUDIT MODULE */

    @Bean
    @ConditionalOnProperty(prefix = "synthetic.human.audit", name = "mode", havingValue = "kafka")
    public KafkaTemplate<String, String> kafkaTemplate() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());

        ProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(factory);
    }

    @Bean
    @ConditionalOnProperty(prefix = "synthetic.human.audit", name = "mode", havingValue = "kafka")
    public AuditKafkaProducer auditKafkaProducer(KafkaTemplate<String, String> kafkaTemplate){
        return new AuditKafkaProducer(kafkaTemplate);
    }

    @Bean
    @ConditionalOnProperty(prefix = "synthetic.human.audit", name = "mode", havingValue = "kafka")
    public KafkaAuditAspect kafkaAuditAspect(AuditKafkaProducer producer){
        return new KafkaAuditAspect(producer, properties.getAudit().getKafka().getTopic());
    }

    @Bean
    @ConditionalOnProperty(prefix = "synthetic.human.audit", name = "mode", havingValue = "console")
    public ConsoleAuditAspect consoleAuditAspect(){
        return new ConsoleAuditAspect();
    }

    /* METRICS MODULE */

    @Bean
    public CommandMetricsPublisher commandMetricsPublisher(BlockingQueue<Runnable> queue, MeterRegistry meterRegistry){
        return new CommandMetricsPublisher(queue, meterRegistry);
    }
}
