package com.cn.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cn.exception.GlobalException;
import com.cn.model.RestCode;
import org.apache.hc.core5.http.ContentType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
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
import java.net.URI;
import java.util.stream.Collectors;

/**
 * 全局异常处理 (RFC 7807 ProblemDetail 格式用于 AJAX/JSON 请求)
 * @author ngcly
 * @since 2019/5/18 14:53
 */
@ControllerAdvice
public class ExceptionHandle {
    private static final Log log = LogFactory.get();

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ProblemDetail> handlerException(HttpServletRequest request, Exception e) throws Exception {
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
                || "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return getProblemDetail(request, e);
        } else {
            throw e;
        }
    }

    public ResponseEntity<ProblemDetail> getProblemDetail(HttpServletRequest request, Exception e) {
        ProblemDetail pd;
        switch (e) {
            case AccessDeniedException accessDeniedException -> 
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, RestCode.UNAUTHORIZED.msg);
            case DataIntegrityViolationException dataIntegrityViolationException -> 
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, RestCode.UNION_DUMP.msg);
            case HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException -> 
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, RestCode.METHOD_ERROR.msg);
            case MissingPathVariableException missingPathVariableException -> 
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, RestCode.NOT_FOUND.msg);
            case MissingServletRequestParameterException missingServletRequestParameterException -> 
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, RestCode.PARAM_ERROR.msg);
            case HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException -> 
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, RestCode.HEAD_ERROR.msg);
            case org.springframework.web.servlet.resource.NoResourceFoundException noResourceFoundException -> 
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, RestCode.NOT_FOUND.msg);
            case GlobalException globalException -> {
                pd = ProblemDetail.forStatusAndDetail(globalException.getStatus(), globalException.getMessage());
                pd.setProperty("code", globalException.getCode());
            }
            case ConstraintViolationException exception -> {
                String msg = exception.getConstraintViolations().stream().map(constraint ->
                        constraint.getInvalidValue() + ":" + constraint.getMessage()).collect(Collectors.joining(";"));
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
            }
            case MethodArgumentNotValidException exception -> {
                String msg = exception.getBindingResult().getAllErrors().stream().map(objectError -> {
                    if (objectError instanceof FieldError fieldError) {
                        return fieldError.getField() + ":" + (objectError.getDefaultMessage() == null ? "" : objectError.getDefaultMessage());
                    }
                    return objectError.getDefaultMessage() == null ? "" : objectError.getDefaultMessage();
                }).collect(Collectors.joining(";"));
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
            }
            case null, default -> 
                pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, RestCode.SERVER_ERROR.msg);
        }
        pd.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(pd.getStatus()).body(pd);
    }
}
