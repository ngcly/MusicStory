package com.cn.config;

import com.cn.dto.RestCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义未认证 401 返回值
 * @author chen
 * @date 2018-03-01 11:03
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        RestCode restCode = RestCode.UNAUTHEN;
        if("Full authentication is required to access this resource".equals(authException.getMessage())){
        }else if("Bad credentials".equals(authException.getMessage())){
          restCode = RestCode.USER_ERR;
        }else if("User is disabled".equals(authException.getMessage())){
            restCode = RestCode.USER_DISABLE;
        }
        out.write("{\"code\":"+restCode.code+",\"msg\":\""+restCode.msg+"\"}");
        out.flush();
        out.close();
    }
}
