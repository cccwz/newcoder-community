package com.nowcoder.community.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

//@Component
//@Aspect
public class AlphaAspect {
    @Pointcut("execution(* com.nowcoder.community.service.*.* (..))")
    public void  pointCut(){

    }
    @Before("pointCut()")
    public void before(){
        System.out.println("before");
    }

    @After("pointCut()")
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("pointCut()")
    public void afterReturning(){
        System.out.println("AfterReturning");
    }
}
