package com.cn.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

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
    public static String getIpAddresses(String ip)
            throws UnsupportedEncodingException {
        // 这里调用pconline的接口
        String urlStr = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip="+ip;
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = HttpUtil.sendHttpGet(urlStr);
        if (returnStr != null) {
            // 处理返回的省市区信息
            String[] temp = returnStr.split(",");
            if(temp.length<3){
                return "";//无效IP，局域网测试
            }
            String region = (temp[4].split(":"))[1].replaceAll("\"", "");
            System.out.println(region);
            region = decodeUnicode(region);// 省份
            String country = "";
            String city = "";
            String county = "";
            String isp = "";
            for (int i = 0; i < temp.length; i++) {
                switch (i) {
                    case 3:
                        country = (temp[i].split(":"))[1].replaceAll("\"", "");
                        country = decodeUnicode(country);// 国家
                        break;
                    case 4:
                        region = (temp[i].split(":"))[1].replaceAll("\"", "");
                        region =  decodeUnicode(region);// 省份
                        break;
                    case 5:
                        city = (temp[i].split(":"))[1].replaceAll("\"", "");
                        city =  decodeUnicode(city);// 市区
                        break;
                    case 6:
                        county = (temp[i].split(":"))[1].replaceAll("\"", "");
                        county =  decodeUnicode(county);// 地区
                        break;
                    case 7:
                        isp = (temp[i].split(":"))[1].replaceAll("\"", "");
                        isp = decodeUnicode(isp); // ISP公司
                        break;
                    default:break;
                }
            }
            if(region!=null){
                if(region.equals("新疆")){
                    region +="维吾尔自治区" ;
                }else if(region.equals("广西")){
                    region +="壮族自治区" ;
                }else if(region.equals("西藏") || region.equals("内蒙古")){
                    region +="自治区" ;
                }else if(region.equals("宁夏")){
                    region +="回族自治区" ;
                }else if(region.equals("澳门") || region.equals("香港")){
                    region +="特别行政区" ;
                }else if(region.equals("上海") ||region.equals("北京") ||region.equals("天津") ||region.equals("重庆")  ){
                    region += "市";
                }else{
                    region +="省";
                }
            }
            if(city!=null){
                city +="市";
            }
            return country+region+city+county+isp;
        }
        return "";
    }


    /**
     * unicode 转换成 中文
     *
     * @author fanhui 2007-3-15
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

}
