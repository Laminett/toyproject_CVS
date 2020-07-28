package com.alliex.cvs.config.aop;

import com.alliex.cvs.util.HttpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class ControllerAspect {

    private static final Logger  logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Before("execution(* com.alliex.cvs.web.*Controller.*(..))")
    public void before(JoinPoint joinPoint) throws Exception {
        List<Object> args = new ArrayList<>();

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletRequest) {
                arg = HttpUtils.requestParametersToString((HttpServletRequest) arg);
            }

            if(arg instanceof HttpServletResponse) {
                continue;
            }

            args.add(arg);
        }

        logger.info("[" + joinPoint.getSignature().toShortString() + "] Controller Parameters : " + args);
    }

}