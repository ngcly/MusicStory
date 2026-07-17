package com.cn.model;

import org.springframework.http.HttpStatus;

/**
 * restful 风格 错误编码
 * @author ngcly
 */
public enum RestCode {
    SUCCESS(200, HttpStatus.OK, "操作成功"),
    VERIFY_CODE_ERR(400, HttpStatus.BAD_REQUEST, "验证码错误或过期"),
    USER_ERR(400, HttpStatus.BAD_REQUEST, "用户名或密码错误"),
    TOKEN_EXPIRE(401, HttpStatus.UNAUTHORIZED, "Token过期"),
    USER_DISABLE(403, HttpStatus.FORBIDDEN, "用户被禁用"),
    USER_LOCKED(403, HttpStatus.FORBIDDEN, "用户被锁定"),
    USER_EXPIRE(403, HttpStatus.FORBIDDEN, "账号过期"),
    PASSWORD_EXPIRE(403, HttpStatus.FORBIDDEN, "密码过期"),
    UNAUTHORIZED(403, HttpStatus.FORBIDDEN, "权限不足"),
    UNION_DUMP(409, HttpStatus.CONFLICT, "唯一标识符重复"),
    PARAM_ERROR(400, HttpStatus.BAD_REQUEST, "参数不合法"),
    NOT_LOGIN(401, HttpStatus.UNAUTHORIZED, "未登录"),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "路径错误"),
    HEAD_ERROR(415, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "头部不匹配"),
    METHOD_ERROR(405, HttpStatus.METHOD_NOT_ALLOWED, "不支持该请求方式"),
    SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "服务异常"),
    FILE_UPLOAD_ERR(500, HttpStatus.INTERNAL_SERVER_ERROR, "文件上传失败");

    public final int code;
    public final HttpStatus status;
    public final String msg;

    RestCode(int code, String msg) {
        this(code, HttpStatus.valueOf(code >= 100 && code < 600 ? code : 400), msg);
    }

    RestCode(int code, HttpStatus status, String msg) {
        this.code = code;
        this.status = status;
        this.msg = msg;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

