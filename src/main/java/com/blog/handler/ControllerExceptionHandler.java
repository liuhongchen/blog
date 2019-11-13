package com.blog.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截所有controller 的方法
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private Logger logger= LoggerFactory.getLogger(ControllerExceptionHandler.class);


    /**
     * 拦截所有exception做统一的处理
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        logger.error("Request URL :{},Exception: {}",request.getRequestURL(),e);

        //这里进行异常判断，主要看ResponseStatus，因为我们在NotFoundException中自己定义了异常状态码为404，则不为空，就会抛出让springboot处理
        //然后，Springboot就会自动返回我们定义的404页面
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null){
            throw e;
        }
        //如果上面的的判断false，则统一处理
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return  mv;
    }
}
