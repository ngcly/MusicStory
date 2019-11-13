package com.cn.config;

import com.cn.pojo.RestCode;
import com.cn.util.RestUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局统一异常处理
 * @author ngcly
 */
@ControllerAdvice
public class ExceptionHandle
        extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value
//            = { IllegalArgumentException.class, IllegalStateException.class })
//    protected ResponseEntity<Object> handleConflict(
//            RuntimeException ex, WebRequest request) {
//        String bodyOfResponse = "This should be application specific";
//        return handleExceptionInternal(ex, bodyOfResponse,
//                new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }

    /**
     * 全局异常处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ModelMap handlerException(HttpServletRequest request, Exception e){
        e.printStackTrace();
        if (e instanceof AccessDeniedException) {
            return RestUtil.failure(RestCode.UNAUTHZ);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return RestUtil.failure(RestCode.METHOD_ERROR);
        } else if (e instanceof MissingPathVariableException) {
            // 缺少路径参数
            return RestUtil.failure(RestCode.NOT_FOUND);
        } else if (e instanceof MissingServletRequestParameterException) {
            // 缺少必须的请求参数
            return RestUtil.failure(RestCode.PARAM_ERROR);
        } else if (e instanceof HttpMediaTypeNotAcceptableException){
            return RestUtil.failure(RestCode.HEAD_ERROR);
        }else if (e instanceof GlobalException){
            return RestUtil.failure(((GlobalException) e).getCode(),e.getMessage());
        } else {
            return RestUtil.failure(RestCode.SERVER_ERROR);
        }

    }
}