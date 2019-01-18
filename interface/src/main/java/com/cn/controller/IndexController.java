package com.cn.controller;

import com.cn.EssayService;
import com.cn.NoticeService;
import com.cn.UserService;
import com.cn.config.JwtTokenProvider;
import com.cn.dto.LogInDTO;
import com.cn.dto.RestCode;
import com.cn.dto.SignUpDTO;
import com.cn.entity.User;
import com.cn.util.RestUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * 测试
 *
 * @author chen
 * @create 2018-08-05 14:48
 */
@Api(value = "IndexController", description = "首页内容相关API")
@RestController
public class IndexController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    UserService userService;
    @Autowired
    EssayService essayService;
    @Autowired
    NoticeService noticeService;

    /**
     * 登陆
     * @param loginDTO
     * @return
     */
    @ApiOperation(value = "登录", notes = "用户登录")
    @PostMapping("/signin")
    public ModelMap authenticateUser(@Valid @RequestBody LogInDTO loginDTO, BindingResult result) {
        if(result.hasErrors()){
            return RestUtil.Error(RestCode.PARAM_ERROR);
        }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            Map<String,String> map = new HashMap<>();
            map.put("tokenType","Bearer");
            map.put("accessToken",jwt);
            return RestUtil.Success(map);
    }

    /**
     * 注册
     * @param signUpDTO
     * @return
     */
    @ApiOperation(value = "注册", notes = "用户注册")
    @PostMapping("/signup")
    @ApiResponses(value={
            @ApiResponse(code=400,message="参数不合法"),
            @ApiResponse(code=500,message="服务器异常")
    })
    public ModelMap registerUser(@Valid @RequestBody SignUpDTO signUpDTO, BindingResult result) {
        if(result.hasErrors()){
            return RestUtil.Error(RestCode.PARAM_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(user, signUpDTO);
        return userService.signUp(user);
    }

    /**
     * 获取文章列表
     */
    @ApiOperation(value = "文章列表", notes = "获取首页文章简介列表")
    @GetMapping("/essay/{pageSize}/{page}")
    public ModelMap getEssayList(@PathVariable int pageSize,@PathVariable int page){
        return essayService.getEssayList(page,pageSize);
    }

    /**
     * 根据id获取文章详情
     */
    @ApiOperation(value = "文章详情", notes = "根据文章Id获取文章详情")
    @GetMapping("/essay/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "文章ID",paramType = "path",dataType = "string")
    })
    public ModelMap getEssayDetail(@PathVariable String id,@PathVariable Integer page){
        return essayService.getEssayDetail(id);
    }

    /**
     * 获取文章评论
     * @param id
     * @return
     */
    @GetMapping("/comments/{id}/{page}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "文章ID",paramType = "path",dataType = "string"),
            @ApiImplicitParam(name = "page",value = "页数",paramType = "path",dataType = "int")
    })
    public ModelMap getEssayComment(@PathVariable String id,@PathVariable Integer page){
        //TODO
        return null;
    }

    /**
     * 获取轮播图
     */
    @ApiOperation(value = "轮播图", notes = "获取轮播图列表")
    @GetMapping("/carousel")
    public ModelMap getCarousel(){
        //TODO
        return null;
    }

    /**
     * 获取公告
     */
    @ApiOperation(value = "公告", notes = "获取展示公告")
    @GetMapping("/notice")
    public ModelMap getNotice(){
        return noticeService.getNotice();
    }
}
