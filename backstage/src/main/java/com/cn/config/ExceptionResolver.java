package com.cn.config;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.cn.pojo.RestCode;
import com.cn.util.RestUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 * @author ngcly
 * @since 2019/5/18 14:53
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //判断是否为ajax请求
        if (request.getHeader(Header.ACCEPT.toString()).contains(ContentType.JSON.toString())
                ||  "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            //返回json
            ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
            mv.addAllObjects(getResult(ex));
            return mv;
        }else{
            //返回 null 时 spring 会自动查询其它的实现类 直到返回 ModelAndView
            return null;
        }
    }

    public ModelMap getResult(Exception e){
        if(e instanceof AccessDeniedException){
           return RestUtil.failure(RestCode.UNAUTHORIZED);
        } else if (e instanceof DataIntegrityViolationException){
            return RestUtil.failure(RestCode.UNION_DUMP);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return RestUtil.failure(RestCode.METHOD_ERROR);
        } else if (e instanceof MissingPathVariableException) {
            // 缺少路径参数
            return RestUtil.failure(RestCode.NOT_FOUND);
        } else if (e instanceof MissingServletRequestParameterException) {
            // 缺少必须的请求参数
            return RestUtil.failure(RestCode.PARAM_ERROR);
        } else if (e instanceof HttpMediaTypeNotAcceptableException){
            return RestUtil.failure(RestCode.HEAD_ERROR);
        }  else if (e instanceof GlobalException){
            return RestUtil.failure(((GlobalException) e).getCode(),e.getMessage());
        } else if (e instanceof ConstraintViolationException) {
            //@RequestParam 参数校验失败
            ConstraintViolationException exception = (ConstraintViolationException) e;
            String msg = exception.getConstraintViolations().stream().map(constraint -> constraint.getInvalidValue()+":"+constraint.getMessage()).collect(Collectors.joining(";"));
            return RestUtil.failure(400, msg);
        } else if (e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            StringBuilder errMsg = new StringBuilder();
            String msg = exception.getBindingResult().getAllErrors().stream().map(objectError -> {
                if(objectError instanceof FieldError){
                    errMsg.append(((FieldError) objectError).getField()).append(":");
                }
                errMsg.append(objectError.getDefaultMessage()==null?"":objectError.getDefaultMessage());
                return errMsg;
            }).collect(Collectors.joining(";"));
            return RestUtil.failure(RestCode.PARAM_ERROR.code, msg);
        } else {
            return RestUtil.failure(RestCode.SERVER_ERROR);
        }
    }
}
