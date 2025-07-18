package com.projectsky.synthetichumancorestarter.audit.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/*
* Модуль, инкапсулирующий сквозную логику для методов, помеченных аннотацией WeylandWatchingYou
* */

@Aspect
@Slf4j
public class ConsoleAuditAspect {

    /**
     * @param joinPoint - Точка в рантайме, где аспект выполняет логику
     * Вывод названия метода, и параметров в лог до завершения работы метода
     * @Before - тип Advice, выполняющийся до вызова target метода
     * */
    @Before("@annotation(com.projectsky.synthetichumancorestarter.audit.WeylandWatchingYou)")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String methodName = signature.getMethod().getName();
        log.info("Вызван метод: {}", methodName);
        log.info("Параметры: {}", Arrays.toString(args));
    }

    /**
     * @param joinPoint - Точка в рантайме, где аспект выполняет логику
     * @param result - Результат работы целевого метода
     * @AfterReturning - тип Advice, выполняющийся после успешного завершения метода
     * */
    @AfterReturning(
            value = "@annotation(com.projectsky.synthetichumancorestarter.audit.WeylandWatchingYou)",
            returning = "result")
    public void audit(JoinPoint joinPoint, Object result) {
        log.info("Результат: {}", result);
    }
}
