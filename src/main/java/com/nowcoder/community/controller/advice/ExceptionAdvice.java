package com.nowcoder.community.controller.advice;

import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private final static Logger LOGGER= LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handlerException(Exception e, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        LOGGER.error("服务器发生异常"+e.getMessage());
        StackTraceElement[] stackTrace = e.getStackTrace();
        for(StackTraceElement element:stackTrace){
            LOGGER.error(element.toString());
        }
        //判断普通请求还是异步请求
        String xRequestedWith = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(xRequestedWith)){
            //ajax
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer=response.getWriter();
            writer.write(CommunityUtil.getJSONString(1,"服务器异常"));
        }else{
            //为null  同步请求
            response.sendRedirect(request.getContextPath()+"/error");
        }
    }

}
