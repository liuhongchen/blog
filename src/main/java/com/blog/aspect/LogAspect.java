package com.blog.aspect;


import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 切面类
 */
@Aspect
@Component
public class LogAspect {
    private final Logger logger= LoggerFactory.getLogger(LogAspect.class);


    /**
     * 这个是定义切点
     */
    @Pointcut("execution(* com.blog.controller.*.*(..))")
    public void log(){

    }


    @Before("log()")
    public void doBefore(){
        logger.info("----------doBefore-----------");
    }


    @After("log()")
    public void doAfter(){
        logger.info("----------doAfter------------");
    }


    @AfterReturning(returning ="result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("result : {}",result);
    }
}
