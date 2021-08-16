package com.cn.config;

import com.cn.pojo.RestCode;
import com.cn.util.RestUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.ModelMap;
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
            return RestUtil.failure(RestCode.UNAUTHORIZED);
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
        } else if (e instanceof ConstraintViolationException) {
            //@RequestParam 参数校验失败
            ConstraintViolationException exception = (ConstraintViolationException) e;
            String msg = exception.getConstraintViolations().stream().map(constraint -> constraint.getInvalidValue()+":"+constraint.getMessage()).collect(Collectors.joining(";"));
            return RestUtil.failure(400, msg);
        } else if (e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            StringBuilder errMsg = new StringBuilder();
            String msg = exception.getBindingResult().getAllErrors().stream().map(objectError -> {
                if(objectError instanceof FieldError){
                    errMsg.append(((FieldError) objectError).getField()).append(":");
                }
                errMsg.append(objectError.getDefaultMessage()==null?"":objectError.getDefaultMessage());
                return errMsg;
            }).collect(Collectors.joining(";"));
            return RestUtil.failure(RestCode.PARAM_ERROR.code, msg);
        } else {
            return RestUtil.failure(RestCode.SERVER_ERROR);
        }
    }

}