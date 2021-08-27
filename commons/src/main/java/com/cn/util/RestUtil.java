package com.cn.util;

import com.cn.pojo.RestCode;
import org.springframework.ui.ModelMap;

/**
 * Rest 风格 格式工具类
 * @author ngcly
 * @since 2018-01-05 11:15
 */
public class RestUtil {

    /**
     * 成功返回
     */
    public static ModelMap success() {
        return success(null);
    }

    /**
     * 一般成功 统一返回
     */
    public static ModelMap success(Object obj) {
        return success(RestCode.SUCCESS.msg,obj);
    }

    /**
     * 自定义成功消息
     */
    public static ModelMap success(String msg, Object obj) {
        return success(RestCode.SUCCESS.code,msg,obj);
    }

    /**
     * 自定义成功返回
     */
    public static ModelMap success(int code,String msg,Object obj){
        ModelMap map = new ModelMap();
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", obj);
        return map;
    }

    /**
     * layui 表格返回
     */
    public static ModelMap success(Long count,Object obj){
        return success(obj).addAttribute("count",count);
    }

    /**
     * 一般失败 统一返回
     */
    public static ModelMap failure(RestCode restCode) {
        return failure(restCode.code,restCode.msg);
    }

    /**
     * 自定义失败信息
     */
    public static ModelMap failure(int code,String msg) {
        ModelMap mp = new ModelMap();
        mp.put("code", code);
        mp.put("msg", msg);
        return mp;
    }
}
