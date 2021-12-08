package com.cn.controller;

import cn.hutool.http.Header;
import com.cn.*;
import com.cn.config.JwtTokenUtil;
import com.cn.entity.*;
import com.cn.pojo.LogInDTO;
import com.cn.pojo.SignUpDTO;
import com.cn.util.RestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

/**
 * 首页控制层
 *
 * @author ngcly
 * @since 2018-08-05 14:48
 */
@Tag(name = "IndexController", description = "首页内容相关API")
@RestController
@RequiredArgsConstructor
public class IndexController {
    private final AuthenticationManager authenticationManager;
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
     *
     * @param request  请求request
     * @param logInDTO 登录参数
     * @return ModelMap
     */
    @Operation(summary = "登录", description = "普通登录")
    @PostMapping("/signin")
    public ModelMap postAccessToken(HttpServletRequest request, @Valid @RequestBody LogInDTO logInDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(logInDTO.getUsername(), logInDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(user);
        logService.saveLog(user.getId(), user.getUsername(), LoginLog.USER_TYPE_CUSTOMER, request);
        return RestUtil.success(token);
    }

    @Operation(summary = "刷新token", description = "用户刷新token")
    @GetMapping("/signin")
    public ModelMap getAccessToken(HttpServletRequest request) {
        return RestUtil.success(jwtTokenUtil.refreshToken(request.getHeader(Header.AUTHORIZATION.getValue())));
    }

    @Operation(summary = "登出", description = "退出登录")
    @DeleteMapping("/signout")
    public ModelMap logout(HttpServletRequest request) {
        JwtTokenUtil.getToken(request);
        return RestUtil.success();
    }

    /**
     * 请求跳转到授权页
     *
     * @param source   三方标识
     * @param response 请求response
     * @throws IOException IO异常
     */
    @Operation(summary = "跳转三方授权页", description = "请求跳转到三方授权页")
    @GetMapping("/render/{source}")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        response.sendRedirect(userService.getSocialRedirectUrl(source));
    }

    /**
     * 第三方回调前端后 前端再调这个 进行登录
     *
     * @param source  三方类型标识
     * @param request 请求request
     * @return ModelMap
     */
    @Operation(summary = "登录", description = "三方用户登录")
    @GetMapping("/login/{source}")
    public ModelMap login(@PathVariable("source") String source, HttpServletRequest request) {
        User userDetail = userService.socialLogin(source, request.getParameter("code"), request.getParameter("state"));
        String token = jwtTokenUtil.generateToken(userDetail);
        logService.saveLog(userDetail.getId(), userDetail.getUsername(), LoginLog.USER_TYPE_CUSTOMER, request);
        return RestUtil.success(token);
    }

    /**
     * 解除绑定
     *
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
     *
     * @param signUpDTO 注册参数
     * @return ModelMap 注册结果
     */
    @Operation(summary = "注册", description = "用户注册")
    @PostMapping("/signup")
    public ModelMap registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        signUpDTO.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        User user = new User();
        BeanUtils.copyProperties(signUpDTO, user);
        return RestUtil.success(userService.signUp(user));
    }

    @Operation(summary = "激活", description = "用户注册激活")
    @GetMapping("/active/{code}")
    public ModelMap activeUser(@PathVariable("code") String code) {
        return RestUtil.success(userService.activeUser(code));
    }

    /**
     * 获取文章列表
     */
    @Operation(summary = "文章列表", description = "获取首页文章简介列表")
    @GetMapping("/essay/{pageSize}/{page}")
    public ModelMap getEssayList(@PathVariable int pageSize, @PathVariable int page) {
        return RestUtil.success(essayService.getEssayList(page, pageSize));
    }

    /**
     * 根据id获取文章详情
     */
    @Operation(summary = "文章详情", description = "根据文章Id获取文章详情")
    @GetMapping("/essay/{id}")
    @Parameters({
            @Parameter(name = "id", description = "文章ID", in = ParameterIn.PATH)
    })
    public ModelMap getEssayDetail(@PathVariable Long id) {
        Essay essay = essayService.getEssayDetail(id);
        return RestUtil.success(essay);
    }

    @Operation(summary = "阅读文章", description = "阅读文章 阅读数+1")
    @PutMapping("/essay/{id}")
    @Parameters({
            @Parameter(name = "id", description = "文章ID", in = ParameterIn.PATH)
    })
    public ModelMap readEssay(@PathVariable Long id) {
        essayService.readEssay(id);
        return RestUtil.success();
    }

    /**
     * 获取文章评论
     */
    @Operation(summary = "获取文章评论", description = "根据文章Id和页数获取评论")
    @GetMapping("/comments/{id}/{page}")
    @Parameters({
            @Parameter(name = "id", description = "文章ID", in = ParameterIn.PATH),
            @Parameter(name = "page", description = "页数", in = ParameterIn.PATH)
    })
    public ModelMap getEssayComment(@PathVariable Long id, @PathVariable Integer page) {
        return RestUtil.success(essayService.getComments(id, page));
    }

    @Operation(summary = "获取分类列表", description = "获取文章分类列表")
    @GetMapping("/classify")
    public ModelMap getClassifyList() {
        return RestUtil.success(classifyService.getClassifyList());
    }

    /**
     * 获取轮播图
     */
    @Operation(summary = "轮播图", description = "获取轮播图列表")
    @GetMapping("/carousel")
    public ModelMap getCarousel() {
        CarouselCategory carouselCategory = carouselService.getCarouselDetail(1L);
        if (Objects.nonNull(carouselCategory)) {
            return RestUtil.success(carouselCategory.getCarousels());
        }
        return RestUtil.success();
    }

    /**
     * 获取公告
     */
    @Operation(summary = "公告", description = "获取展示公告")
    @GetMapping("/notice")
    public ModelMap getNotice() {
        return RestUtil.success(noticeService.getNotice());
    }

    /**
     * 全文搜索
     */
    @Operation(summary = "搜索", description = "文章搜索")
    @GetMapping("/search/{pageSize}/{page}/{keyword}")
    @Parameters({
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.PATH),
    })
    public ModelMap search(@PathVariable int pageSize, @PathVariable int page, @PathVariable("keyword") String keyword) {
        return RestUtil.success(bookService.highLightSearchEssay(keyword, PageRequest.of(page - 1, pageSize)));
    }

    /**
     * 测试使用 初始化ES数据
     */
    @Operation(summary = "初始化ES数据", description = "将数据库数据同步至ES 测试用")
    @GetMapping("/init/es/data")
    public ModelMap initEsDataTest() {
        essayService.initBookDataTest();
        return RestUtil.success();
    }
}