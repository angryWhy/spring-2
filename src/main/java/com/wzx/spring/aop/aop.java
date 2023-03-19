package com.wzx.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class aop {
    @Before(value="execution(public void com.wzx.spring.aop.aop.fn())")
    public void fn(JoinPoint joinPoint){
        System.out.println("这是前置通知！");
    }
}
