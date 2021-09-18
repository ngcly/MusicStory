package com.cn.controller;

import com.cn.*;
import com.cn.config.JwtTokenUtil;
import com.cn.entity.*;
import com.cn.pojo.LogInDTO;
import com.cn.pojo.SignUpDTO;
import com.cn.pojo.UserDetail;
import com.cn.util.RestUtil;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

/**
 * 描述:
 * 测试
 * @author ngcly
 * @since 2018-08-05 14:48
 */
@Api(value = "IndexController", tags = "首页内容相关API")
@RestController
@RequiredArgsConstructor
public class IndexController {
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EssayService essayService;
    private final NoticeService noticeService;
    private final CarouselService carouselService;
    private final ClassifyService classifyService;
    private final BookService bookService;
    private final LogService logService;

    /**
     * 用户登录
     * @param request 请求request
     * @param logInDTO 登录参数
     * @return ModelMap
     */
    @ApiOperation(value = "登录", notes = "普通登录")
    @PostMapping("/signin")
    public ModelMap postAccessToken(HttpServletRequest request, @Valid @RequestBody LogInDTO logInDTO) {
        UserDetail user = userService.login(logInDTO.getUsername(),logInDTO.getPassword());
        String token = jwtTokenUtil.generateToken(user);
        logService.saveLog(user.getId(),user.getUsername(), LoginLog.USER_TYPE_CUSTOMER, request);
        return RestUtil.success(token);
    }

    @ApiOperation(value = "刷新token", notes = "用户刷新token")
    @GetMapping("/signin")
    public ModelMap getAccessToken(HttpServletRequest request) {
        return RestUtil.success(jwtTokenUtil.refreshToken(request.getHeader("authorization")));
    }

    @ApiOperation(value = "登出", notes = "退出登录")
    @DeleteMapping("/signout")
    public ModelMap logout(HttpServletRequest request){
        JwtTokenUtil.getToken(request);
        return RestUtil.success();
    }

    /**
     * 请求跳转到授权页
     * @param source 三方标识
     * @param response 请求response
     * @throws IOException IO异常
     */
    @ApiOperation(value = "跳转三方授权页", notes = "请求跳转到三方授权页")
    @GetMapping("/render/{source}")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        response.sendRedirect(userService.getSocialRedirectUrl(source));
    }

    /**
     * 第三方回调前端后 前端再调这个 进行登录
     * @param source 三方类型标识
     * @param request 请求request
     * @return ModelMap
     */
    @ApiOperation(value = "登录", notes = "三方用户登录")
    @GetMapping("/login/{source}")
    public ModelMap login(@PathVariable("source") String source,HttpServletRequest request) {
        UserDetail userDetail = userService.socialLogin(source,request.getParameter("code"),request.getParameter("state"));
        String token = jwtTokenUtil.generateToken(userDetail);
        logService.saveLog(userDetail.getId(),userDetail.getUsername(), LoginLog.USER_TYPE_CUSTOMER, request);
        return RestUtil.success(token);
    }

    /**
     * 解除绑定
     * @param openid 三方openid
     * @return ModelMap
     */
    @PutMapping("/revoke/{openid}")
    public ModelMap revokeSocial(@PathVariable("openid") String openid) {
        userService.revokeSocial(openid);
        return RestUtil.success();
    }

    /**
     * 注册
     * @param signUpDTO 注册参数
     * @return ModelMap 注册结果
     */
    @ApiOperation(value = "注册", notes = "用户注册")
    @PostMapping("/signup")
    public ModelMap registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        signUpDTO.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        User user = new User();
        BeanUtils.copyProperties(signUpDTO, user);
        return RestUtil.success(userService.signUp(user));
    }

    @ApiOperation(value = "激活", notes = "用户注册激活")
    @GetMapping("/active/{code}")
    public ModelMap activeUser(@PathVariable("code")String code){
        return RestUtil.success(userService.activeUser(code));
    }

    /**
     * 获取文章列表
     */
    @ApiOperation(value = "文章列表", notes = "获取首页文章简介列表")
    @GetMapping("/essay/{pageSize}/{page}")
    public ModelMap getEssayList(@PathVariable int pageSize,@PathVariable int page){
        return RestUtil.success(essayService.getEssayList(page,pageSize));
    }

    /**
     * 根据id获取文章详情
     */
    @ApiOperation(value = "文章详情", notes = "根据文章Id获取文章详情")
    @GetMapping("/essay/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "文章ID",paramType = "path",dataType = "string")
    })
    public ModelMap getEssayDetail(@PathVariable Long id){
        Essay essay = essayService.getEssayDetail(id);
        return RestUtil.success(essay);
    }

    @ApiOperation(value = "阅读文章", notes = "阅读文章 阅读数+1")
    @PutMapping("/essay/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "文章ID",paramType = "path",dataType = "string")
    })
    public ModelMap readEssay(@PathVariable Long id){
        essayService.readEssay(id);
        return RestUtil.success();
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
    public ModelMap getEssayComment(@PathVariable Long id,@PathVariable Integer page){
        return RestUtil.success(essayService.getComments(id,page));
    }

    @ApiOperation(value = "获取分类列表", notes = "获取文章分类列表")
    @GetMapping("/classify")
    public ModelMap getClassifyList(){
        return RestUtil.success(classifyService.getClassifyList());
    }

    /**
     * 获取轮播图
     */
    @ApiOperation(value = "轮播图", notes = "获取轮播图列表")
    @GetMapping("/carousel")
    public ModelMap getCarousel(){
        CarouselCategory carouselCategory = carouselService.getCarouselDetail(1L);
        if(Objects.nonNull(carouselCategory)){
            return RestUtil.success(carouselCategory.getCarousels());
        }
        return RestUtil.success();
    }

    /**
     * 获取公告
     */
    @ApiOperation(value = "公告", notes = "获取展示公告")
    @GetMapping("/notice")
    public ModelMap getNotice(){
        return RestUtil.success(noticeService.getNotice());
    }

    /**
     * 全文搜索
     */
    @ApiOperation(value = "搜索", notes = "文章搜索")
    @GetMapping("/search/{pageSize}/{page}/{keyword}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword",value = "关键字",paramType = "path",dataType = "string"),
    })
    public ModelMap search(@PathVariable int pageSize,@PathVariable int page,@PathVariable("keyword")String keyword){
        return RestUtil.success(bookService.highLightSearchEssay(keyword, PageRequest.of(page - 1, pageSize)));
    }

    /**
     * 测试使用 初始化ES数据
     */
    @ApiOperation(value = "初始化ES数据", notes = "将数据库数据同步至ES 测试用")
    @GetMapping("/init/es/data")
    public ModelMap InitEsDataTest(){
        essayService.initBookDataTest();
        return RestUtil.success();
    }
}