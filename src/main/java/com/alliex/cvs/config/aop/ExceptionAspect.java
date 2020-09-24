package com.alliex.cvs.config.aop;

//@Aspect
//@Component
//public class ExceptionAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);
//
//    @AfterThrowing(pointcut = "execution(* com.alliex.cvs.web.*Controller.*(..))", throwing = "ex")
//    public void exceptionLog(JoinPoint joinPoint, Exception ex) {
//        logger.error("Exception occurred. " + joinPoint.getSignature().toShortString());
//        logger.error("Exception message : {}" + ex.getMessage());
//
//        List<Object> args = new ArrayList<>();
//        for (Object arg : joinPoint.getArgs()) {
//            if (arg instanceof HttpServletRequest) {
//                arg = HttpUtils.requestParametersToString((HttpServletRequest) arg);
//            }
//
//            if (arg instanceof HttpServletResponse) {
//                continue;
//            }
//
//            args.add(arg);
//        }
//
//        logger.info("Exception parameters : " + args);
//
//        ex.printStackTrace();
//    }
//
//}