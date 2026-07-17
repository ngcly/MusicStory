package com.cn.controller;

import com.cn.user.UserService;
import com.cn.content.EssayService;
import com.cn.content.CarouselService;
import com.cn.content.ClassifyService;
import com.cn.notification.NoticeService;
import com.cn.search.BookService;
import com.cn.config.JwtTokenUtil;
import com.cn.config.MyAuthenticationToken;
import com.cn.config.SecurityUser;
import com.cn.user.domain.User;
import com.cn.entity.*;
import com.cn.model.AuthenticationDetails;
import com.cn.model.LogInDTO;
import com.cn.model.SignUpDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Map;
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

    /**
     * 用户登录
     *
     * @param request  请求request
     * @param logInDTO 登录参数
     * @return Result
     */
    @Operation(summary = "登录", description = "普通登录")
    @PostMapping("/signin")
    public ResponseEntity<User> postAccessToken(HttpServletRequest request, @Valid @RequestBody LogInDTO logInDTO) {
        AbstractAuthenticationToken authenticationToken = getAuthenticationToken(request, logInDTO);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user = securityUser.getUser();
        String token = jwtTokenUtil.generateToken(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(user);
    }

    private AbstractAuthenticationToken getAuthenticationToken(HttpServletRequest request, LogInDTO logInDTO) {
        AbstractAuthenticationToken authenticationToken;
        if (logInDTO instanceof LogInDTO.SocialLoginDTO dto) {
            authenticationToken = new MyAuthenticationToken(dto.getLoginType().name(), dto.getCode(), dto.getState());
        } else {
            LogInDTO.UserNameLoginDTO dto = (LogInDTO.UserNameLoginDTO) logInDTO;
            authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(dto.getUsername(), dto.getPassword());
        }

        authenticationToken.setDetails(new AuthenticationDetails(request));
        return authenticationToken;
    }

    @Operation(summary = "刷新token", description = "用户刷新token")
    @GetMapping("/signin")
    public String getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return jwtTokenUtil.refreshToken(authorization);
    }

    @Operation(summary = "登出", description = "退出登录")
    @DeleteMapping("/signout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    /**
     * 请求跳转到授权页
     *
     * @param source   三方标识
     */
    @Operation(summary = "跳转三方授权页", description = "请求跳转到三方授权页")
    @GetMapping("/render/{source}")
    public ResponseEntity<Void> renderAuth(@Valid @PathVariable("source") String source) {
        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .header(HttpHeaders.LOCATION, userService.getSocialRedirectUrl(source))
                .build();
    }

    /**
     * 解除绑定
     *
     * @param openid 三方openid
     */
    @PutMapping("/revoke/{openid}")
    public ResponseEntity<Void> revokeSocial(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable("openid") String openid) {
        User user = securityUser.getUser();
        userService.revokeSocial(user.getId(), openid);
        return ResponseEntity.ok().build();
    }

    /**
     * 注册
     *
     * @param signUpDTO 注册参数
     * @return 注册结果描述
     */
    @Operation(summary = "注册", description = "用户注册")
    @PostMapping("/signup")
    public String registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        signUpDTO.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        User user = new User();
        BeanUtils.copyProperties(signUpDTO, user);
        return userService.signUp(user);
    }

    @Operation(summary = "激活", description = "用户注册激活")
    @GetMapping("/active/{code}")
    public ResponseEntity<Void> activeUser(@PathVariable("code") String code) {
        userService.activeUser(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/signin"))
                .build();
    }

    /**
     * 获取文章列表
     */
    @Operation(summary = "文章列表", description = "获取首页文章简介列表")
    @GetMapping("/essay/{pageSize}/{page}")
    public List<Map<String,Object>> getEssayList(@PathVariable int pageSize, @PathVariable int page) {
        return essayService.getEssayList(page, pageSize);
    }

    /**
     * 根据id获取文章详情
     */
    @Operation(summary = "文章详情", description = "根据文章Id获取文章详情")
    @GetMapping("/essay/{id}")
    @Parameter(name = "id", description = "文章ID", in = ParameterIn.PATH)
    public Essay getEssayDetail(@PathVariable Long id) {
        return essayService.getEssayDetail(id);
    }

    @Operation(summary = "阅读文章", description = "阅读文章 阅读数+1")
    @PutMapping("/essay/{id}")
    @Parameter(name = "id", description = "文章ID", in = ParameterIn.PATH)
    public ResponseEntity<Void> readEssay(@PathVariable Long id) {
        essayService.readEssay(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取文章评论
     */
    @Operation(summary = "获取文章评论", description = "根据文章Id和页数获取评论")
    @GetMapping("/comments/{id}/{page}")
    @Parameter(name = "id", description = "文章ID", in = ParameterIn.PATH)
    @Parameter(name = "page", description = "页数", in = ParameterIn.PATH)
    public Page<Map<String,Object>> getEssayComment(@PathVariable Long id, @PathVariable Integer page) {
        return essayService.getComments(id, page);
    }

    @Operation(summary = "获取分类列表", description = "获取文章分类列表")
    @GetMapping("/classify")
    public List<Classify> getClassifyList() {
        return classifyService.getClassifyList();
    }

    /**
     * 获取轮播图
     */
    @Operation(summary = "轮播图", description = "获取轮播图列表")
    @GetMapping("/carousel")
    public List<Carousel> getCarousel() {
        CarouselCategory carouselCategory = carouselService.getCarouselDetail(1L);
        if (Objects.nonNull(carouselCategory)) {
            return carouselCategory.getCarousels();
        }
        return List.of();
    }

    /**
     * 获取公告
     */
    @Operation(summary = "公告", description = "获取展示公告")
    @GetMapping("/notice")
    public List<Notice> getNotice() {
        return noticeService.getNotice();
    }

    /**
     * 全文搜索
     */
    @Operation(summary = "搜索", description = "文章搜索")
    @GetMapping("/search/{pageSize}/{page}/{keyword}")
    @Parameter(name = "keyword", description = "关键字", in = ParameterIn.PATH)
    public Page<Book> search(@PathVariable int pageSize, @PathVariable int page, @PathVariable("keyword") String keyword) {
        return bookService.highLightSearchEssay(keyword, PageRequest.of(page - 1, pageSize));
    }

    /**
     * 测试使用 初始化ES数据
     */
    @Operation(summary = "初始化ES数据", description = "将数据库数据同步至ES 测试用")
    @GetMapping("/init/es/data")
    public ResponseEntity<Void> initEsDataTest() {
        essayService.initBookDataTest();
        return ResponseEntity.ok().build();
    }
}
