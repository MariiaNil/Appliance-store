package com.epam.rd.autocode.assessment.appliances.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Controller;

@Aspect
@Component
@Slf4j
public class LoggingServices {

    @Around("execution(* com.epam.rd.autocode.assessment.appliances.service..*(..)) " +
            "|| execution(* com.epam.rd.autocode.assessment.appliances.controller..*(..)) " +
            "|| execution(* com.epam.rd.autocode.assessment.appliances.config..*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Starting execution of method: {} with parameters: {}", methodName, args);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("Method completed: {} in {} ms", methodName, duration);
            return result;
        } catch (Throwable ex) {
            log.error("Error in method: {}. Message: {}", methodName, ex.getMessage());
            throw ex;
        }
    }

    @Around("@annotation(com.epam.rd.autocode.assessment.appliances.aspect.Loggable)")
    public Object logMethodCallForLoggable(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Starting execution of method: {} with parameters: {}", methodName, args);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("Method completed: {} in {} ms", methodName, duration);
            return result;
        } catch (Throwable ex) {
            log.error("Error in method: {}. Message: {}", methodName, ex.getMessage());
            throw ex;
        }
    }

}
