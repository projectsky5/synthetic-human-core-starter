package com.projectsky.synthetichumancorestarter.audit.aop;

import com.projectsky.synthetichumancorestarter.audit.AuditKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
@ConditionalOnProperty(prefix = "audit", name = "mode", havingValue = "kafka")
@RequiredArgsConstructor
public class KafkaAuditAspect {

    private final AuditKafkaProducer auditKafkaProducer;

    @Value("${audit.topic:audit-topic}")
    private String topic;

    /**
     * @param joinPoint - Точка входа в рантайме, позволяющая управлять выполнением метода
     * @Around - Тип Advice, дающий полный контроль над выполнением логики
     * */
    @Around("@annotation(com.projectsky.synthetichumancorestarter.audit.WeylandWatchingYou)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String methodName = signature.getMethod().getName();
        Object proceed = joinPoint.proceed();

        String message = String.format(
                "Вызван метод: %s\nПараметры: %s\nРезультат: %s",
                methodName, Arrays.toString(args), proceed
        );

        auditKafkaProducer.send(topic, message);

        return proceed;
    }
}
