package com.cn.config;

import com.cn.dto.RestCode;
import com.cn.util.RestUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Controller Rest风格统一异常处理
 */
@ControllerAdvice
public class RestResponseExceptionHandler
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
     * 方法级访问拒绝处理 @PreAuthorize，@ PostAuthorize和@Secure Access Denied
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        return ResponseEntity.ok(RestUtil.Error(RestCode.UNAUTHZ));
    }
}