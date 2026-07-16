package com.cn.config;

import com.cn.exception.GlobalException;
import com.cn.model.RestCode;
import com.cn.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * 全局统一异常处理
 * @author ngcly
 */
@Slf4j
@RestControllerAdvice
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
    public Result<String> handlerException(HttpServletRequest request, Exception e){
        if (e instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
            log.warn("No static resource found: {}", e.getMessage());
            return Result.failure(RestCode.NOT_FOUND);
        } else if (e instanceof BadCredentialsException) {
            log.warn("Bad credentials: {}", e.getMessage());
            return Result.failure(RestCode.USER_ERR);
        } else if (e instanceof AccessDeniedException) {
            log.warn("Access denied for URI {}: {}", request.getRequestURI(), e.getMessage());
            return Result.failure(RestCode.UNAUTHORIZED);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            log.warn("Http request method not supported for URI {}: {}", request.getRequestURI(), e.getMessage());
            return Result.failure(RestCode.METHOD_ERROR);
        } else if (e instanceof MissingPathVariableException) {
            // 缺少路径参数
            log.warn("Missing path variable for URI {}: {}", request.getRequestURI(), e.getMessage());
            return Result.failure(RestCode.NOT_FOUND);
        } else if (e instanceof MissingServletRequestParameterException) {
            // 缺少必须的请求参数
            log.warn("Missing request parameter for URI {}: {}", request.getRequestURI(), e.getMessage());
            return Result.failure(RestCode.PARAM_ERROR);
        } else if (e instanceof HttpMediaTypeNotAcceptableException){
            log.warn("Http media type not acceptable for URI {}: {}", request.getRequestURI(), e.getMessage());
            return Result.failure(RestCode.HEAD_ERROR);
        } else if (e instanceof GlobalException exception){
            log.warn("Global exception for URI {}: {}", request.getRequestURI(), exception.getMessage());
            return Result.failure(exception.getCode(),exception.getMessage());
        } else if (e instanceof ConstraintViolationException exception) {
            //@RequestParam 参数校验失败
            log.warn("Constraint violation for URI {}: {}", request.getRequestURI(), exception.getMessage());
            return Result.failure(RestCode.PARAM_ERROR.code, exception.getMessage());
        } else if (e instanceof MethodArgumentNotValidException exception){
            log.warn("Method argument not valid for URI {}: {}", request.getRequestURI(), exception.getMessage());
            return Result.failure(RestCode.PARAM_ERROR.code, exception.getMessage());
        } else {
            log.error(request.getRequestURI(), e);
            return Result.failure(RestCode.SERVER_ERROR);
        }
    }

//    @ExceptionHandler
//    ProblemDetail handException(GlobalException e) {
//        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getCode());
//        problemDetail.setDetail(e.getMessage());
//        return problemDetail;
//    }
}