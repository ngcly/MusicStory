package com.cn.config;

import com.cn.exception.GlobalException;
import com.cn.model.RestCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.util.stream.Collectors;

/**
 * 全局统一异常处理 (RFC 7807 ProblemDetail 规范)
 * @author ngcly
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {

    /**
     * 全局异常处理，使用 RFC 7807 ProblemDetail
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ProblemDetail> handleAllExceptions(HttpServletRequest request, Exception e) {
        ProblemDetail problemDetail;

        if (e instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
            log.warn("No static resource found: {}", e.getMessage());
            problemDetail = createProblemDetail(HttpStatus.NOT_FOUND, RestCode.NOT_FOUND.msg, e.getMessage(), request);
        } else if (e instanceof BadCredentialsException) {
            log.warn("Bad credentials: {}", e.getMessage());
            problemDetail = createProblemDetail(HttpStatus.BAD_REQUEST, RestCode.USER_ERR.msg, "用户名或密码错误", request);
        } else if (e instanceof AccessDeniedException) {
            log.warn("Access denied for URI {}: {}", request.getRequestURI(), e.getMessage());
            problemDetail = createProblemDetail(HttpStatus.FORBIDDEN, RestCode.UNAUTHORIZED.msg, "权限不足", request);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            log.warn("Http request method not supported for URI {}: {}", request.getRequestURI(), e.getMessage());
            problemDetail = createProblemDetail(HttpStatus.METHOD_NOT_ALLOWED, RestCode.METHOD_ERROR.msg, e.getMessage(), request);
        } else if (e instanceof MissingPathVariableException) {
            log.warn("Missing path variable for URI {}: {}", request.getRequestURI(), e.getMessage());
            problemDetail = createProblemDetail(HttpStatus.BAD_REQUEST, RestCode.PARAM_ERROR.msg, e.getMessage(), request);
        } else if (e instanceof MissingServletRequestParameterException) {
            log.warn("Missing request parameter for URI {}: {}", request.getRequestURI(), e.getMessage());
            problemDetail = createProblemDetail(HttpStatus.BAD_REQUEST, RestCode.PARAM_ERROR.msg, e.getMessage(), request);
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            log.warn("Http media type not acceptable for URI {}: {}", request.getRequestURI(), e.getMessage());
            problemDetail = createProblemDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, RestCode.HEAD_ERROR.msg, e.getMessage(), request);
        } else if (e instanceof GlobalException exception) {
            log.warn("Global exception for URI {}: {}", request.getRequestURI(), exception.getMessage());
            problemDetail = createProblemDetail(exception.getStatus(), "业务异常", exception.getMessage(), request);
            problemDetail.setProperty("code", exception.getCode());
        } else if (e instanceof ConstraintViolationException exception) {
            log.warn("Constraint violation for URI {}: {}", request.getRequestURI(), exception.getMessage());
            problemDetail = createProblemDetail(HttpStatus.BAD_REQUEST, RestCode.PARAM_ERROR.msg, exception.getMessage(), request);
        } else if (e instanceof MethodArgumentNotValidException exception) {
            log.warn("Method argument not valid for URI {}: {}", request.getRequestURI(), exception.getMessage());
            String msg = exception.getBindingResult().getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            problemDetail = createProblemDetail(HttpStatus.BAD_REQUEST, RestCode.PARAM_ERROR.msg, msg, request);
        } else {
            log.error(request.getRequestURI(), e);
            problemDetail = createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, RestCode.SERVER_ERROR.msg, "服务器内部错误", request);
        }

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    private ProblemDetail createProblemDetail(HttpStatusCode status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return problemDetail;
    }
}