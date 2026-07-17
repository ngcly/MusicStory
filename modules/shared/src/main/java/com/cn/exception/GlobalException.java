package com.cn.exception;

import com.cn.model.RestCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * @author ngcly
 * 自定义全局异常
 * @since 2019/5/18 11:58
 */
public class GlobalException extends RuntimeException {
    private final int code;
    private final HttpStatusCode status;
    private final String message;

    public GlobalException(RestCode restCode){
        this(restCode.code, restCode.getStatus(), restCode.msg);
    }

    public GlobalException(String msg){
        this(RestCode.SERVER_ERROR.code, HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }

    public GlobalException(int code, String msg) {
        this(code, HttpStatus.valueOf(code >= 100 && code < 600 ? code : 400), msg);
    }

    public GlobalException(int code, HttpStatusCode status, String msg) {
        super(msg);
        this.code = code;
        this.status = status;
        this.message = msg;
    }

    public int getCode() {
        return code;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

