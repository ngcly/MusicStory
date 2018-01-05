package com.cn.util;

import org.springframework.ui.ModelMap;

/**
 * Rest 风格 格式工具类
 *
 * @author chen
 * @date 2018-01-05 11:15
 */
public class RestUtil {


    /**
     * 一般成功 统一返回
     */
    public static ModelMap Success(Object obj) {
        ModelMap mp = new ModelMap();
        mp.put("code", 200);
        mp.put("state", "success");
        mp.put("msg", "操作成功");
        mp.put("data", obj);
        return mp;
    }

    /**
     * 自定义成功消息
     */
    public static ModelMap Success(String msg, Object obj) {
        ModelMap map = new ModelMap();
        map.put("code", 200);
        map.put("state", "success");
        map.put("msg", msg);
        map.put("data", obj);
        return map;
    }

    /**
     * 一般错误 统一返回
     */
    public static ModelMap Error(int code) {
        ModelMap map = new ModelMap();
        map.put("code", code);
        map.put("state", "error");
        map.put("msg", "操作失败");
        return map;
    }

    /**
     * 自定义错误信息
     */
    public static ModelMap Error(int code,String msg) {
        ModelMap mp = new ModelMap();
        mp.put("code", code);
        mp.put("state", "error");
        mp.put("msg", msg);
        return mp;
    }
}
