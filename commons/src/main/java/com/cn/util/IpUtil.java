package com.cn.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * IP 获取工具类
 *
 * @author ngcly
 * @date 2018-01-05 10:38
 */
@Component
public class IpUtil {
    @Autowired
    RestTemplate restTemplate;

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
        String returnStr;
        String url;
        //阿里云接口
//        url = "https://dm-81.data.aliyun.com/rest/160601/ip/getIpInfo.json?ip="+ip;
//		Map<String, String> headers = new HashMap<>(1);
//		headers.put("Authorization", "APPCODE " + appCode);
//        returnStr = HttpUtil.sendHttpGet(url,headers);
        // 淘宝接口
        url = "http://ip.taobao.com/service/getIpInfo.php?ip="+ip;
        returnStr = HttpUtil.sendHttpGet(url);
        JSONObject json = JSONObject.parseObject(returnStr);
        if("0".equals(json.getString("code"))){
            JSONObject data = json.getJSONObject("data");
            return data.getString("country")+data.getString("area")+data.getString("region")+data.getString("city");
        }

        return null;
    }
}
