package com.projectsky.synthetichumancorestarter.config;

import com.projectsky.synthetichumancorestarter.audit.AuditKafkaProducer;
import com.projectsky.synthetichumancorestarter.audit.aop.ConsoleAuditAspect;
import com.projectsky.synthetichumancorestarter.audit.aop.KafkaAuditAspect;
import com.projectsky.synthetichumancorestarter.command.model.Command;
import com.projectsky.synthetichumancorestarter.command.service.CommandExecutor;
import com.projectsky.synthetichumancorestarter.command.service.CommandService;
import com.projectsky.synthetichumancorestarter.metrics.CommandMetricsPublisher;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Configuration
@EnableConfigurationProperties(SyntheticProperties.class)
@ConditionalOnProperty(prefix = "synthetic.human.core", name = "enabled", havingValue = "true")
public class SyntheticHumanAutoConfiguration {

    private final SyntheticProperties properties;

    public SyntheticHumanAutoConfiguration(SyntheticProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockingQueue<Command> commandQueue(){
        return new LinkedBlockingQueue<>(properties.getCommand().getQueueCapacity());
    }

    @Bean
    @ConditionalOnMissingBean
    public ExecutorService commandExecutorService(BlockingQueue<Runnable> queue){
        return new ThreadPoolExecutor(
                properties.getCommand().getCorePoolSize(),
                properties.getCommand().getMaxPoolSize(),
                properties.getCommand().getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                queue
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandExecutor commandExecutor(CommandMetricsPublisher commandMetricsPublisher){
        return new CommandExecutor(commandMetricsPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandService commandService(ExecutorService executorService, CommandExecutor executor){
        return new CommandService(executorService, executor);
    }

    //

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "synthetic.human.audit", name = "mode", havingValue = "kafka")
    public KafkaTemplate<String, String> kafkaTemplate(){
        Map<String, Object> props = new HashMap<>();
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                properties.getAudit().getKafka().getBootstrapServers());
        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(
                ProducerConfig.ACKS_CONFIG,
                properties.getAudit().getKafka().getAcks());
        ProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(factory);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "synthetic.human.audit", name = "mode", havingValue = "kafka")
    public AuditKafkaProducer auditKafkaProducer(KafkaTemplate<String, String> kafkaTemplate){
        return new AuditKafkaProducer(kafkaTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "synthetic.human.audit", name = "mode", havingValue = "kafka")
    public KafkaAuditAspect kafkaAuditAspect(AuditKafkaProducer producer){
        return new KafkaAuditAspect(producer, properties.getAudit().getTopic());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "synthetic.human.audit", name = "mode", havingValue = "console")
    public ConsoleAuditAspect consoleAuditAspect(){
        return new ConsoleAuditAspect();
    }

    //

    @Bean
    @ConditionalOnMissingBean
    public CommandMetricsPublisher commandMetricsPublisher(BlockingQueue<Command> queue, MeterRegistry meterRegistry){
        return new CommandMetricsPublisher(queue, meterRegistry);
    }
}
