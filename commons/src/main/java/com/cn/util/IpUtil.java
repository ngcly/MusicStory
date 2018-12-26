package com.cn.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * IP 获取工具类
 *
 * @author chen
 * @date 2018-01-05 10:38
 */
public class IpUtil {

    public static String getIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 根据ip获取城市
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getIpAddresses(String ip) {
        //阿里云接口
        String url = "https://dm-81.data.aliyun.com/rest/160601/ip/getIpInfo.json?ip="+ip;
		String appcode = "4bf399ed20194960aa55e569c9d8af06";
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "APPCODE " + appcode);
        String returnStr = HttpUtil.sendHttpGet(url,headers);
        // 这里调用淘宝的接口
//        String urlStr = "http://ip.taobao.com/service/getIpInfo.php?ip="+ip;
//        String returnStr = HttpUtil.sendHttpGet(urlStr);
        JSONObject json = JSONObject.parseObject(returnStr);
        if("0".equals(json.getString("code"))){
            JSONObject data = json.getJSONObject("data");
            return data.getString("country")+data.getString("area")+data.getString("region")+data.getString("city");
        }
        return null;
    }

}
