package com.cn.controller;

import com.cn.CarouselService;
import com.cn.EssayService;
import com.cn.NoticeService;
import com.cn.UserService;
import com.cn.config.JwtTokenProvider;
import com.cn.pojo.LogInDTO;
import com.cn.pojo.SignUpDTO;
import com.cn.entity.Carousel;
import com.cn.entity.CarouselCategory;
import com.cn.entity.Essay;
import com.cn.entity.User;
import com.cn.util.RestUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * 测试
 *
 * @author chen
 * @create 2018-08-05 14:48
 */
@Api(value = "IndexController", tags = "首页内容相关API")
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
    @Autowired
    CarouselService carouselService;

    /**
     * 登陆
     * @param loginDTO
     * @return
     */
    @ApiOperation(value = "登录", notes = "用户登录")
    @PostMapping("/signin")
    public ModelMap authenticateUser(@Valid @RequestBody LogInDTO loginDTO, BindingResult result) {
        if(result.hasErrors()){
            return RestUtil.failure(400,result.getFieldError().getField()+":"+result.getFieldError().getDefaultMessage());
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
            return RestUtil.success(map);
    }

    /**
     * 注册
     * @param signUpDTO
     * @return
     */
    @ApiOperation(value = "注册", notes = "用户注册")
    @PostMapping("/signup")
    public ModelMap registerUser(@Valid @RequestBody SignUpDTO signUpDTO, BindingResult result) {
        if(result.hasErrors()){
            return RestUtil.failure(400,result.getFieldError().getField()+":"+result.getFieldError().getDefaultMessage());
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
    public ModelMap getEssayDetail(@PathVariable String id){
        Essay essay = essayService.getEssayDetail(id);
        return RestUtil.success(essay);
    }

    /**
     * 获取文章评论
     */
    @ApiOperation(value = "获取文章评论", notes = "根据文章Id和页数获取评论")
    @GetMapping("/comments/{id}/{page}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "文章ID",paramType = "path",dataType = "string"),
            @ApiImplicitParam(name = "page",value = "页数",paramType = "path",dataType = "int")
    })
    public ModelMap getEssayComment(@PathVariable String id,@PathVariable Integer page){
        return RestUtil.success(essayService.getComments(id,page));
    }

    /**
     * 获取轮播图
     */
    @ApiOperation(value = "轮播图", notes = "获取轮播图列表")
    @GetMapping("/carousel")
    public ModelMap getCarousel(){
        CarouselCategory carouselCategory = carouselService.getCarouselDetail("index");
        List<Carousel> carousels = null;
        if(carouselCategory!=null){
            carousels = carouselCategory.getCarousels();
        }
        return RestUtil.success(carousels);
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
