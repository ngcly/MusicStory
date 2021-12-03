package com.cn.pojo;

/**
 * restful 风格 错误编码
 * @author ngcly
 */
public enum RestCode {
    SUCCESS(0, "操作成功"),
    VERIFY_CODE_ERR(408,"验证码错误或过期"),
    USER_ERR(407,"用户名或密码错误"),
    TOKEN_EXPIRE(409,"Token过期"),
    USER_DISABLE(334,"用户被禁用"),
    USER_LOCKED(335,"用户被锁定"),
    USER_EXPIRE(336,"账号过期"),
    PASSWORD_EXPIRE(337,"密码过期"),
    UNAUTHORIZED(301 ,"权限不足"),
    UNION_DUMP(333, "唯一标识符重复"),
    PARAM_ERROR(400, "参数不合法"),
    NOT_LOGIN(401, "未登录"),
    NOT_FOUND(404 ,"路径错误"),
    HEAD_ERROR(415, "头部不匹配"),
    METHOD_ERROR(405 ,"不支持该请求方式"),
    SERVER_ERROR(500, "服务异常"),
    FILE_UPLOAD_ERR(611,"文件上传失败");

    public final int code;
    public final String msg;

    RestCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
