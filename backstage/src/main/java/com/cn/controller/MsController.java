package com.cn.controller;

import com.cn.*;
import com.cn.entity.*;
import com.cn.util.Result;
import com.cn.util.UploadUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 音书管理 控制类
 *
 * @author ngcly
 */
@Controller
@RequestMapping("/ms")
@AllArgsConstructor
public class MsController {
    private final UserService userService;
    private final RoleService roleService;
    private final ClassifyService classifyService;
    private final NoticeService noticeService;
    private final CarouselService carouselService;
    private final EssayService essayService;

    /**
     * 用户列表页
     */
    @RequestMapping("/user")
    public String userList() {
        return "user/userList";
    }

    /**
     * 获取用户列表信息
     */
    @ResponseBody
    @RequestMapping("/userList")
    public Result<List<User>> getUserList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size, User user) {
        Page<User> userList = userService.getUserList(PageRequest.of(page - 1, size), user);
        return Result.success(userList.getTotalElements(), userList.getContent());
    }

    /**
     * 用户编辑页
     */
    @RequestMapping("/userEdit")
    public String userEdit(@RequestParam Long userId, Model model) {
        User user = userService.getUserDetail(userId);
        Set<Role> roles = roleService.getAvailableRoles(Role.ROLE_TYPE_CUSTOMER);
        roles.addAll(user.getRoleList());
        String checkRoleIds = user.getRoleList().stream().map(role ->
                role.getId().toString()).collect(Collectors.joining(","));
        model.addAttribute(user);
        //待选角色列表
        model.addAttribute("roles", roles);
        //已勾选角色ID
        model.addAttribute("checkRoleId", checkRoleIds);
        return "user/userEdit";
    }

    /**
     * 用户详情页
     */
    @RequestMapping("/userDetail")
    public String userDetail(@RequestParam Long userId, Model model) {
        User user = userService.getUserDetail(userId);
        model.addAttribute(user);
        return "user/userDetail";
    }

    /**
     * 修改用户
     */
    @ResponseBody
    @RequestMapping("/userSave")
    public Result<?> saveUser(@Valid User user) {
        userService.altUser(user);
        return Result.success();

    }

    /**
     * 删除用户
     */
    @ResponseBody
    @RequestMapping("/userDel")
    public Result<?> delUser(@RequestParam Long userId) {
        userService.delUser(userId);
        return Result.success();
    }

    /**
     * 分类列表页
     */
    @RequestMapping("/classify")
    public String classifyList() {
        return "classify/classifyList";
    }

    /**
     * 获取分类列表
     */
    @ResponseBody
    @RequestMapping("/classifyList")
    public Result<List<Classify>> getClassifyList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size, Classify classify) {
        Page<Classify> classifyList = classifyService.getClassifyList(PageRequest.of(page - 1, size), classify);
        return Result.success(classifyList.getTotalElements(), classifyList.getContent());
    }

    /**
     * 修改分类页
     */
    @RequestMapping("/classifyAlt")
    public String altClassify(@RequestParam(required = false) Long id, Model model) {
        Classify classify = new Classify();
        if (id != null) {
            classify = classifyService.getClassifyDetail(id);
        }
        model.addAttribute(classify);
        return "classify/classifyEdit";
    }

    /**
     * 保存分类
     */
    @ResponseBody
    @PostMapping("/saveClassify")
    public Result<?> saveClassify(@Valid Classify classify) {
        classifyService.saveClassify(classify);
        return Result.success();

    }

    /**
     * 删除分类
     */
    @ResponseBody
    @GetMapping("/classifyDel/{id}")
    public Result<?> delClassify(@PathVariable("id") Long id) {
        classifyService.deleteClassify(id);
        return Result.success();
    }

    /**
     * 文章列表页
     */
    @RequestMapping("/essay")
    public String essayList(Model model) {
        model.addAttribute("classifyList", classifyService.getClassifyList());
        return "essay/essayList";
    }

    /**
     * 获取文章列表
     */
    @ResponseBody
    @RequestMapping("/essayList")
    public Result<List<Essay>> getEssayList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size, Essay essay) {
        Page<Essay> essayList = essayService.getEssayList(PageRequest.of(page - 1, size, Sort.by("createdAt").descending()), essay);
        return Result.success(essayList.getTotalElements(), essayList.getContent());
    }

    /**
     * 文章审查页
     */
    @RequestMapping("/essayAlt")
    public String essayAlt(@RequestParam Long id, Model model) {
        Essay essay = essayService.getEssayDetail(id);
        model.addAttribute(essay);
        return "essay/essayEdit";
    }

    /**
     * 文章审查
     */
    @ResponseBody
    @PostMapping("/essaySave")
    public Result<?> essaySave(@Valid Essay essay) {
        essayService.altEssayState(essay);
        return Result.success();
    }

    /**
     * 公告列表页
     */
    @RequestMapping("/notice")
    public String noticeList() {
        return "notice/noticeList";
    }

    /**
     * 获取公告列表
     */
    @ResponseBody
    @GetMapping("/noticeList")
    public Result<List<Notice>> getNoticeList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size, Notice notice) {
        Page<Notice> notices = noticeService.getNoticeList(PageRequest.of(page - 1, size), notice);
        return Result.success(notices.getTotalElements(), notices.getContent());
    }

    /**
     * 修改公告页
     */
    @RequestMapping("/noticeAlt")
    public String altNotice(@RequestParam(required = false) Long id, Model model) {
        Notice notice = new Notice();
        if (id != null) {
            notice = noticeService.getNoticeDetail(id);
        }
        model.addAttribute(notice);
        return "notice/noticeEdit";
    }

    /**
     * 保存公告
     */
    @ResponseBody
    @PostMapping("/saveNotice")
    public Result<?> saveNotice(@Valid Notice notice) {
        noticeService.addOrUpdateNotice(notice);
        return Result.success();
    }

    /**
     * 删除公告
     */
    @ResponseBody
    @GetMapping("/noticeDel/{id}")
    public Result<?> delNotice(@PathVariable("id") Long id) {
        noticeService.deleteNotice(id);
        return Result.success();
    }

    /**
     * 轮播图页
     */
    @RequestMapping("/carousel")
    public String carouselList() {
        return "carousel/carouselList";
    }

    /**
     * 获取轮播图列表
     */
    @ResponseBody
    @GetMapping("/carouselList")
    public Result<List<CarouselCategory>> carouselList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size, String name) {
        Page<CarouselCategory> carouselCategory = carouselService.getCarouselList(name, PageRequest.of(page - 1, size));
        return Result.success(carouselCategory.getTotalElements(), carouselCategory.getContent());
    }

    /**
     * 修改轮播分类页
     */
    @RequestMapping("/carouselCategoryAlt")
    public String altCarouselCategory(@RequestParam(required = false) Long id, Model model) {
        CarouselCategory carouselCategory = new CarouselCategory();
        if (id != null) {
            carouselCategory = carouselService.getCarouselDetail(id);
        }
        model.addAttribute(carouselCategory);
        return "carousel/carouselCategoryEdit";
    }

    /**
     * 修改轮播图页
     */
    @RequestMapping("/carouselAlt")
    public String altCarousel(@RequestParam Long id, Model model) {
        CarouselCategory carouselCategory = carouselService.getCarouselDetail(id);
        model.addAttribute(carouselCategory);
        return "carousel/carouselEdit";
    }

    /**
     * 保存轮播图
     */
    @ResponseBody
    @PostMapping("/saveCarousel")
    public Result<?> saveCarousel(@Valid CarouselCategory carouselCategory) {
        carouselService.addOrUpdateCarousel(carouselCategory);
        return Result.success();
    }

    /**
     * 添加轮播图
     */
    @ResponseBody
    @RequestMapping("/addCarousel")
    public Result<?> addCarousel(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
        if (file.isEmpty()) {
            return Result.failure(222, "文件为空");
        }
        String path = UploadUtil.uploadFileByAli(file, "img");
        carouselService.addCarousel(id, path);
        return Result.success();
    }

    /**
     * 删除轮播分类
     */
    @ResponseBody
    @GetMapping("/carouselDel/{id}")
    public Result<?> delCarouselCategory(@PathVariable("id") Long id) {
        carouselService.deleteCarouselCategory(id);
        return Result.success();
    }

    /**
     * 删除轮播图
     */
    @ResponseBody
    @GetMapping("/carouselDel")
    public Result<?> delCarousel(@RequestParam("id") Long id, @RequestParam("url") String url) {
        carouselService.deleteCarousel(id, url);
        return Result.success();
    }

}
