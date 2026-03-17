package tigerbank.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimingAspect {

    @Around("@annotation(Timed)")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();

        long start = System.nanoTime();
        Object result = joinPoint.proceed();
        double ms = (System.nanoTime() - start) / 1_000_000.0;

        System.out.printf("[Timed] %s — %.3f мс%n", methodName, ms);
        return result;
    }
}
