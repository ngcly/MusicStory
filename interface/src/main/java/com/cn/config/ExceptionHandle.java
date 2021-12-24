package com.cn.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cn.pojo.RestCode;
import com.cn.util.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局统一异常处理
 * @author ngcly
 */
@ControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {
    private static final Log log = LogFactory.get();

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
    public Result<String> handlerException(HttpServletRequest request, Exception e){
        log.error(e);
        if(e instanceof BadCredentialsException) {
            return Result.failure(RestCode.USER_ERR);
        } else if (e instanceof AccessDeniedException) {
            return Result.failure(RestCode.UNAUTHORIZED);
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
        }else if (e instanceof GlobalException exception){
            return Result.failure(exception.getCode(),exception.getMessage());
        } else if (e instanceof ConstraintViolationException exception) {
            //@RequestParam 参数校验失败
            String msg = exception.getConstraintViolations().stream().map(constraint -> constraint.getInvalidValue()+":"+constraint.getMessage()).collect(Collectors.joining(";"));
            return Result.failure(400, msg);
        } else if (e instanceof MethodArgumentNotValidException exception){
            StringBuilder errMsg = new StringBuilder();
            String msg = exception.getBindingResult().getAllErrors().stream().map(objectError -> {
                if(objectError instanceof FieldError){
                    errMsg.append(((FieldError) objectError).getField()).append(":");
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