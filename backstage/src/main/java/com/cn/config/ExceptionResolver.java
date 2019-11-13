package com.cn.config;

import com.cn.pojo.RestCode;
import com.cn.util.HttpUtil;
import com.cn.util.RestUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ngcly
 * @Classname ExceptionResolver
 * @Description 全局异常处理
 * @Date 2019/5/18 14:53
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //判断是否为ajax请求
        if (HttpUtil.isAjaxRequest(request)) {
            //返回json
            ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
            ModelMap modelMap;
            if(ex instanceof AccessDeniedException){
                modelMap = RestUtil.failure(RestCode.UNAUTHZ);
            } else if(ex instanceof GlobalException){
                modelMap = RestUtil.failure(((GlobalException) ex).getCode(),ex.getMessage());
            } else {
                modelMap = RestUtil.failure(RestCode.SERVER_ERROR);
            }
            mv.addAllObjects(modelMap);
            return mv;
        }else{
            //返回 null 时 spring 会自动查询其它的实现类 直到返回 ModelAndView
            return null;
        }
    }
}
