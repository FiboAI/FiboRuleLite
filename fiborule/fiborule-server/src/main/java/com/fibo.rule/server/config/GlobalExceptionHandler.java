package com.fibo.rule.server.config;

import com.alibaba.fastjson.JSON;
import com.fibo.rule.server.enums.ErrorCodeEnum;
import com.fibo.rule.server.exception.ApiException;
import com.fibo.rule.server.model.ResponseEntityBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常处理大管家
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 不支持的请求方式
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public void handleException(HttpServletRequest request, HttpServletResponse response, HttpRequestMethodNotSupportedException e) {
        log.warn("HttpRequestMethodNotSupportedException:", e);
        write(response, ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR.getCode(), e.getMessage()));
    }

    /**
     * 处理业务参数异常
     */
    @ExceptionHandler(value = ApiException.class)
    public void handleException(HttpServletRequest request, HttpServletResponse response, ApiException e) {
        log.warn("ApiException:", e);
        write(response, ResponseEntityBuilder.buildErrorResponse(e.errCode, e.message));
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(value = RuntimeException.class)
    public void handleException(HttpServletRequest request, HttpServletResponse response, RuntimeException e) {
        log.error("RuntimeException:", e);
        if (null != e.getMessage() && e.getMessage().equals("不允许访问")) {
            write(response, ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR.getCode(), e.getMessage()));
        } else {
            write(response, ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR.getCode(), e.getMessage()));
        }
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(value = Exception.class)
    public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("Exception:", e);
        write(response, ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR.getCode(), e.getMessage()));
    }


    private static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    public static void write(HttpServletResponse response, Object obj) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json;charset=utf-8");
            out = response.getWriter();
            out.println(JSON.toJSONString(obj));
        } catch (IOException e) {
            log.debug("io输出异常", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

}
