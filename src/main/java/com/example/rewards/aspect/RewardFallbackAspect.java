package com.example.rewards.aspect;
 
import com.example.rewards.dto.RewardResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
 
import java.util.Collections;
 
/**
 * Aspect to provide fallback for RewardService methods.
 */
@Aspect
@Component
public class RewardFallbackAspect {
 
	/**
     * Intercepts calls to calculateRewards() and provides fallback response if an exception occurs.
     */
	
    @Around("execution(* com.example.rewards.service.RewardServiceImpl.calculateRewards(..))")
    public Object handleRewardFailure(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            Long customerId = (Long) joinPoint.getArgs()[0];
            return new RewardResponse(customerId, Collections.emptyMap(), 0);
        }
    }
}
 
