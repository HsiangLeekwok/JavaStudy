package com.java.enjoy.spring.cap10.config;

import com.java.enjoy.spring.cap10.aop.Calculator;
import com.java.enjoy.spring.cap10.aop.LogAspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Author: Hsiang Leekwok<br/>
 * Date: 2019/04/01<br/>
 * Description: AOP 相关实现<br/>
 * <p>
 * 日志切面类的方法需要动态感知到div()方法的运行<br/>
 * 通知方法：<br/>
 * 前置通知：logStart(); 在执行div()之前运行(@Before)<br/>
 * 后置通知：logEnd();在执行div()之后运行，不管是否有异常都会执行(@After)<br/>
 * 返回通知：logReturn(); 在目标方法div()正常返回值后运行，出现异常之后不会执行(@AfterReturning)<br/>
 * 异常通知：logException(); 在目标方法div()发生异常时运行(@AfterThrowing)<br/>
 * 环绕通知：动态代理，需要手动执行joinPoint.proceed()才会去执行div()方法，执行div之前相当于前置通知，执行之后相当于后置通知(@Around)<br/></p>
 */
@Configuration
@EnableAspectJAutoProxy
public class Cap10MainConfig {

    @Bean
    public Calculator calculator() {
        return new Calculator();
    }

    @Bean
    public LogAspects logAspects() {
        return new LogAspects();
    }
}
