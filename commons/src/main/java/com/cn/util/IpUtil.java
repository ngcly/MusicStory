package com.cn.util;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * IP 获取工具类
 *
 * @author ngcly
 * @date 2018-01-05 10:38
 */
public final class IpUtil {
    private IpUtil() {
    }

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

    private static final String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String LOCAL_IP = "127.0.0.1";
    private static final String COMMA = ",";
    private static final String UNKNOWN = "unknown";
    private static final String EMPTY = "";
    private static final String IP_ADDRESS_URL = "https://whois.pconline.com.cn/ipJson.jsp?json=true&ip=%s";
    private static final String ADDRESS_KEY = "addr";

    /**
     * 判断ip是否为空，空返回true
     *
     * @param ip IP
     * @return boolean
     */
    public static boolean isEmptyIp(final String ip) {
        return ip == null || ip.length() == 0 || EMPTY.equals(ip.trim()) || UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 判断ip是否不为空，不为空返回true
     *
     * @param ip IP
     * @return boolean
     */
    public static boolean isNotEmptyIp(final String ip) {
        return !isEmptyIp(ip);
    }

    /***
     * 获取客户端ip地址(可以穿透代理)
     * @param request HttpServletRequest
     * @return String
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = EMPTY;
        for (String header : HEADERS) {
            ip = request.getHeader(header);
            if (isNotEmptyIp(ip)) {
                break;
            }
        }
        if (isEmptyIp(ip)) {
            ip = request.getRemoteAddr();
        }
        if (isNotEmptyIp(ip) && ip.contains(COMMA)) {
            ip = ip.split(COMMA)[0];
        }
        if (LOCAL_IPV6.equals(ip)) {
            ip = LOCAL_IP;
        }
        return ip;
    }


    /**
     * 获取本机的局域网ip地址，兼容Linux
     *
     * @return String
     * @throws SocketException 异常
     */
    public String getLocalHostIp() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        String localHostAddress = EMPTY;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = allNetInterfaces.nextElement();
            Enumeration<InetAddress> address = networkInterface.getInetAddresses();
            while (address.hasMoreElements()) {
                InetAddress inetAddress = address.nextElement();
                if (inetAddress instanceof Inet4Address) {
                    localHostAddress = inetAddress.getHostAddress();
                }
            }
        }
        return localHostAddress;
    }

    /**
     * 根据ip获取城市
     *
     * @return String
     */
    public static String getIpAddresses(String ip) {
        String url = String.format(IP_ADDRESS_URL, ip);
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        if(Objects.isNull(restTemplate)) {
            restTemplate = new RestTemplate();
        }
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            JsonNode jsonNode = JacksonUtil.toObject(responseEntity.getBody(), JsonNode.class);
            return jsonNode.get(ADDRESS_KEY).asText();
        }
        return null;
    }
}
