package com.cn.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cn.exception.GlobalException;
import com.cn.model.RestCode;
import com.cn.util.Result;
import org.apache.hc.core5.http.ContentType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 * @author ngcly
 * @since 2019/5/18 14:53
 */
@ControllerAdvice
public class ExceptionHandle {
    private static final Log log = LogFactory.get();

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<String> handlerException(HttpServletRequest request, Exception e) throws Exception {
        if (e instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
            log.warn("No static resource found: {}", e.getMessage());
        } else if (e instanceof AccessDeniedException
                || e instanceof HttpRequestMethodNotSupportedException
                || e instanceof MissingPathVariableException
                || e instanceof MissingServletRequestParameterException
                || e instanceof HttpMediaTypeNotAcceptableException
                || e instanceof ConstraintViolationException
                || e instanceof MethodArgumentNotValidException
                || e instanceof GlobalException) {
            log.warn("Client error: {}", e.getMessage());
        } else {
            log.error(e);
        }
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        if ((accept != null && accept.contains(ContentType.APPLICATION_JSON.getMimeType()))
                ||  "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return getResult(e);
        }else{
            throw e;
        }
    }

    public Result<String> getResult(Exception e){
        switch (e) {
            case AccessDeniedException accessDeniedException -> {
                return Result.failure(RestCode.UNAUTHORIZED);
            }
            case DataIntegrityViolationException dataIntegrityViolationException -> {
                return Result.failure(RestCode.UNION_DUMP);
            }
            case HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException -> {
                return Result.failure(RestCode.METHOD_ERROR);
            }
            case MissingPathVariableException missingPathVariableException -> {
                // 缺少路径参数
                return Result.failure(RestCode.NOT_FOUND);
                // 缺少路径参数
            }
            case MissingServletRequestParameterException missingServletRequestParameterException -> {
                // 缺少必须的请求参数
                return Result.failure(RestCode.PARAM_ERROR);
                // 缺少必须的请求参数
            }
            case HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException -> {
                return Result.failure(RestCode.HEAD_ERROR);
            }
            case org.springframework.web.servlet.resource.NoResourceFoundException noResourceFoundException -> {
                return Result.failure(RestCode.NOT_FOUND);
            }
            case GlobalException globalException -> {
                return Result.failure(globalException.getCode(), globalException.getMessage());
            }
            case ConstraintViolationException exception -> {
                //@RequestParam 参数校验失败
                String msg = exception.getConstraintViolations().stream().map(constraint ->
                        constraint.getInvalidValue() + ":" + constraint.getMessage()).collect(Collectors.joining(";"));
                return Result.failure(RestCode.PARAM_ERROR.code, msg);
            }
            case MethodArgumentNotValidException exception -> {
                StringBuilder errMsg = new StringBuilder();
                String msg = exception.getBindingResult().getAllErrors().stream().map(objectError -> {
                    if (objectError instanceof FieldError fieldError) {
                        errMsg.append(fieldError.getField()).append(":");
                    }
                    errMsg.append(objectError.getDefaultMessage() == null ? "" : objectError.getDefaultMessage());
                    return errMsg;
                }).collect(Collectors.joining(";"));
                return Result.failure(RestCode.PARAM_ERROR.code, msg);
            }
            case null, default -> {
                return Result.failure(RestCode.SERVER_ERROR);
            }
        }
    }
}
