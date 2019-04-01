package com.java.enjoy.spring.cap10.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/04/01<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Description</b>: Log 切面实现类
 */
@Aspect
public class LogAspects {

    @Pointcut("execution(public int com.java.enjoy.spring.cap10.aop.Calculator.*(..))")
    public void pointCut() {

    }
    /**
     * 目标方法执行开始时的切入方法
     */
    @Before("pointCut()")
    public void logStart() {
        System.out.println("method execute before....");
    }

    /**
     * 目标方法执行之后的切入方法
     */
    @After("pointCut()")
    public void logEnd() {
        System.out.println("method execute after....");
    }

    /**
     * 目标方法返回后的切入方法
     */
    @AfterReturning("pointCut()")
    public void logReturn() {
        System.out.println("method execute return....result is: {}");
    }

    /**
     * 目标方法发生异常时的切入方法
     */
    @AfterThrowing("pointCut()")
    public void logThrowing() {
        System.out.println("method execute throwing....Exception is: {}");
    }

    /**
     * 目标方法执行前后的切入方法
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object logAround(ProceedingJoinPoint point) throws Throwable {
        System.out.println("@Around before execute target method");
        Object object = point.proceed();
        System.out.println("@Around after execute target method");
        return object;
    }
}
