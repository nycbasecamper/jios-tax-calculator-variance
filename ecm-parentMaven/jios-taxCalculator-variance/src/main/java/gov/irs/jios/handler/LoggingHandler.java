package gov.irs.jios.handler;

import java.util.Arrays;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author suneetha.pathula
 */

@Aspect
@Component
@Slf4j
public class LoggingHandler {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
    }

    @Pointcut("execution(* *.*(..))")
    protected void allMethod() {
    }

    @Pointcut("execution(public * *(..))")
    protected void loggingPublicOperation() {
    }

    @Pointcut("execution(* *.*(..))")
    protected void loggingAllOperation() {
    }

    @Pointcut("within(gov.irs.jios..*)")
    private void logAnyFunctionWithinResource() {
    }

    //before -> Any resource annotated with @Controller annotation 
    //and all method and function taking HttpServletRequest as first parameter
    //@Before("controller() && allMethod()")
    public void logBefore(JoinPoint joinPoint) {

        log.info("Entering in Method :  " + joinPoint.getSignature().getName());
        log.info("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
        log.info("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
        log.debug("Target class : " + joinPoint.getTarget().getClass().getName());

        Object[] signatureArgs = joinPoint.getArgs();
        if (Objects.nonNull(signatureArgs) && signatureArgs.length > 0) {
            log.info("Request object: " + signatureArgs[0]);
        }
    }

    //@Before("logAnyFunctionWithinResource() && allMethod()")
    public void logBeforeAnyFunction(JoinPoint joinPoint) {

        log.debug("Entering in Method :  " + joinPoint.getSignature().getName());
        log.debug("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
        log.debug("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
        log.debug("Target class : " + joinPoint.getTarget().getClass().getName());

        Object[] signatureArgs = joinPoint.getArgs();
        if (Objects.nonNull(signatureArgs) && signatureArgs.length > 0) {
            log.debug("Arguments : " + signatureArgs[0]);
        }
    }

    //After -> All method within resource annotated with @Controller annotation
    // and return a  value
    //@AfterReturning(pointcut = "logAnyFunctionWithinResource() && allMethod()", returning = "result")
    public void logAfterAnyFunction(JoinPoint joinPoint, Object result) {
        String returnValue = this.getValue(result);
        log.debug("Method return value: " + returnValue);
    }

    //After -> All method within resource annotated with @Controller annotation
    // and return a  value
    //@AfterReturning(pointcut = "controller() && allMethod()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String returnValue = this.getValue(result);
        log.info("Response object: " + returnValue);
    }
    //After -> Any method within resource annotated with @Controller annotation 

    //Around -> Any method within resource annotated with @Controller annotation 
    //@Around("controller() && allMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            log.info("Method " + className + "." + methodName + " ()" + " execution time : "
                    + elapsedTime + " ms");

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
                    + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }

    private String getValue(Object result) {
        String returnValue = null;
        if (null != result) {
            if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
                returnValue = result.toString();
            } else {
                returnValue = result.toString();
            }
        }
        return returnValue;
    }
}
