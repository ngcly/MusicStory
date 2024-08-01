package com.cn.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cn.exception.GlobalException;
import com.cn.model.RestCode;
import com.cn.util.Result;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.springframework.dao.DataIntegrityViolationException;
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
        log.error(e);
        if (request.getHeader(HttpHeaders.ACCEPT).contains(ContentType.APPLICATION_JSON.getMimeType())
                ||  "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return getResult(e);
        }else{
            throw e;
        }
    }

    public Result<String> getResult(Exception e){
        if(e instanceof AccessDeniedException){
            return Result.failure(RestCode.UNAUTHORIZED);
        } else if (e instanceof DataIntegrityViolationException){
            return Result.failure(RestCode.UNION_DUMP);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return Result.failure(RestCode.METHOD_ERROR);
        } else if (e instanceof MissingPathVariableException) {
            // 缺少路径参数
            return Result.failure(RestCode.NOT_FOUND);
        } else if (e instanceof MissingServletRequestParameterException) {
            // 缺少必须的请求参数
            return Result.failure(RestCode.PARAM_ERROR);
        } else if (e instanceof HttpMediaTypeNotAcceptableException){
            return Result.failure(RestCode.HEAD_ERROR);
        }  else if (e instanceof GlobalException globalException){
            return Result.failure(globalException.getCode(), globalException.getMessage());
        } else if (e instanceof ConstraintViolationException exception) {
            //@RequestParam 参数校验失败
            String msg = exception.getConstraintViolations().stream().map(constraint ->
                    constraint.getInvalidValue()+":"+constraint.getMessage()).collect(Collectors.joining(";"));
            return Result.failure(RestCode.PARAM_ERROR.code, msg);
        } else if (e instanceof MethodArgumentNotValidException exception){
            StringBuilder errMsg = new StringBuilder();
            String msg = exception.getBindingResult().getAllErrors().stream().map(objectError -> {
                if(objectError instanceof FieldError fieldError){
                    errMsg.append(fieldError.getField()).append(":");
                }
                errMsg.append(objectError.getDefaultMessage()==null?"":objectError.getDefaultMessage());
                return errMsg;
            }).collect(Collectors.joining(";"));
            return Result.failure(RestCode.PARAM_ERROR.code, msg);
        } else {
            return Result.failure(RestCode.SERVER_ERROR);
        }
    }
}
