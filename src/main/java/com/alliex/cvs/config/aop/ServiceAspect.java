package com.alliex.cvs.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAspect.class);

    @Before("execution(* com.alliex.cvs.service.*Service.*(..))")
    public void before(JoinPoint joinPoint) {
        logger.debug("[" + joinPoint.getSignature().toShortString() + "] --------------------------- Service START ----------------------------------");
        logger.debug("["+ joinPoint.getSignature().toShortString() + "] Service Parameters : " + Arrays.toString(joinPoint.getArgs()));
    }

}