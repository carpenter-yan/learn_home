package com.carpenter.yan.spring.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoleAspect {
    @Pointcut("execution(* com.carpenter.yan.spring.service.impl.RoleServiceImpl.printRole(..))")
    public void printRole(){

    }

    @Before("printRole()")
    public void before(){
        System.out.println("---before---");
    }

    @After("printRole()")
    public void after(){
        System.out.println("---after---");
    }

    @AfterReturning("printRole()")
    public void afterReturning(){
        System.out.println("---afterReturning---");
    }

    @AfterThrowing("printRole()")
    public void afterThrowing(){
        System.out.println("---afterThrowing---");
    }
}
