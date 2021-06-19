package com.nowcoder.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
public class ServiceLogAspect {
    private static final Logger LOGGER= LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.nowcoder.community.service.*.* (..))")
    public void pointCut(){}

    @Before("pointCut()")
    public void before(JoinPoint joinPoint){
        //用户(ip地址) 在 when  访问了  什么功能{什么类  什么方法}
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes==null){
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String ip=request.getRemoteHost();
        String now=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //类名+方法名
        String target= joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        LOGGER.info(String.format("用户[%s],在[%s],访问了[%s]",ip,now,target));

    }
}
