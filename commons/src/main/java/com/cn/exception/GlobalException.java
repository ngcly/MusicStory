package com.cn.exception;

import com.cn.pojo.RestCode;

/**
 * @author ngcly
 * 自定义全局异常
 * @since 2019/5/18 11:58
 */
public class GlobalException extends RuntimeException {
    private final int code;
    private final String message;

    public GlobalException(RestCode restCode){
        this(restCode.code,restCode.msg);
    }

    public GlobalException(String msg){
        this(RestCode.SERVER_ERROR.code,msg);
    }

    public GlobalException(int code, String msg) {
        super(msg);
        this.code = code;
        this.message = msg;
    }

    public int getCode() {
        return code;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
