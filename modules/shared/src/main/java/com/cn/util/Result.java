package com.cn.util;

import com.cn.model.RestCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 返回体
 *
 * @author ngcly
 * @version V1.0
 * @since 2020/5/18 11:04
 */
@Getter
@Schema(description = "返回体")
public class Result<T> {

    @Schema(title = "状态码")
    private final int code;
    @Schema(title = "说明信息")
    private final String msg;
    @Schema(title = "内容")
    private final T data;

    private Result(int code, String msg) {
        this(code, msg, null);
    }

    private Result(int code, String msg, @Nullable T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 默认成功返回
     */
    public static <T> Result<T> success() {
        return new Result<>(RestCode.SUCCESS.code, RestCode.SUCCESS.msg);
    }

    /**
     * 一般成功 统一返回
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(RestCode.SUCCESS.code, RestCode.SUCCESS.msg, data);
    }

    /**
     * 自定义成功消息
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(RestCode.SUCCESS.code, msg, data);
    }

    /**
     * 自定义成功返回
     */
    public static <T> Result<T> success(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    /**
     * layui 表格返回
     */
    public static <T> LayuiResult<T> success(Long count, T data){
        return LayuiResult.ok(count,data);
    }

    /**
     * 一般错误 统一返回
     */
    public static <T> Result<T> failure(RestCode restCode) {
        return new Result<>(restCode.code, restCode.msg);
    }

    /**
     * 自定义错误信息
     */
    public static <T> Result<T> failure(int code, String msg) {
        return new Result<>(code, msg);
    }


    static class LayuiResult<T> extends Result<T> {
        @Getter
        private final Long count;

        private LayuiResult(int code, String msg,T data,Long count) {
            super(code,msg,data);
            this.count = count;
        }

        public static <T> LayuiResult<T> ok(Long count, T data){
            return new LayuiResult<>(RestCode.SUCCESS.code, RestCode.SUCCESS.msg, data, count);
        }
    }

}
