package com.cn.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * IP 获取工具类
 *
 * @author ngcly
 * @date 2018-01-05 10:38
 */
@Component
public class IpUtil {

    private static final String[] HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

    private static final String LOCAL_IPV6="0:0:0:0:0:0:0:1";

    /**
     * 判断ip是否为空，空返回true
     * @param ip
     * @return
     */
    public static boolean isEmptyIp(final String ip){
        return (ip == null || ip.length() == 0 || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip));
    }


    /**
     * 判断ip是否不为空，不为空返回true
     * @param ip
     * @return
     */
    public static boolean isNotEmptyIp(final String ip){
        return !isEmptyIp(ip);
    }

    /***
     * 获取客户端ip地址(可以穿透代理)
     * @param request HttpServletRequest
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = "";
        for (String header : HEADERS) {
            ip = request.getHeader(header);
            if(isNotEmptyIp(ip)) {
                break;
            }
        }
        if(isEmptyIp(ip)){
            ip = request.getRemoteAddr();
        }
        if(isNotEmptyIp(ip) && ip.contains(",")){
            ip = ip.split(",")[0];
        }
        if(LOCAL_IPV6.equals(ip)){
            ip = "127.0.0.1";
        }
        return ip;
    }


    /**
     * 获取本机的局域网ip地址，兼容Linux
     * @return String
     * @throws Exception
     */
    public String getLocalHostIP() throws Exception{
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        String localHostAddress = "";
        while(allNetInterfaces.hasMoreElements()){
            NetworkInterface networkInterface = allNetInterfaces.nextElement();
            Enumeration<InetAddress> address = networkInterface.getInetAddresses();
            while(address.hasMoreElements()){
                InetAddress inetAddress = address.nextElement();
                if(inetAddress != null && inetAddress instanceof Inet4Address){
                    localHostAddress = inetAddress.getHostAddress();
                }
            }
        }
        return localHostAddress;
    }

    /**
     * 根据ip获取城市
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getIpAddresses(String ip) {
        String url;
        // 淘宝接口
        url = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip="+ip;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            JSONObject json = JSONUtil.parseObj(responseEntity.getBody());
            return json.getStr("addr");
        }
        return null;
    }
}
