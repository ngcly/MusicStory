package com.cn.controller;

import com.cn.*;
import com.cn.dto.RestCode;
import com.cn.entity.*;
import com.cn.util.RestUtil;
import com.cn.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Set;


/**
 * 音书管理 控制类
 */
@Controller
@RequestMapping("/ms")
public class MsController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    ClassifyService classifyService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    CarouselService carouselService;


    /**
     * 用户列表页
     */
    @PreAuthorize("hasAuthority('user:view')")
    @RequestMapping("/user")
    public String userList(){
        return "user/userList";
    }

    /**
     * 获取用户列表信息
     */
    @ResponseBody
    @RequestMapping("/userList")
    public ModelMap getUserList(@RequestParam(value="page",defaultValue="1") Integer page,
                                @RequestParam(value="size",defaultValue="10") Integer size, User user){
        Page<User> userList = userService.getUserList(PageRequest.of(page - 1, size), user);
        return RestUtil.Success(userList.getTotalElements(),userList.getContent());
    }

    /**
     * 用户编辑页
     */
    @RequestMapping("/userEdit")
    public String userEdit(@RequestParam String userId, Model model){
        User user = userService.getUserDetail(userId);
        Set<Role> roles = roleService.getAvailableRoles((byte) 2);
        ArrayList<String> checkRoleIds = new ArrayList<>();
        for(Role role:user.getRoleList()){
            checkRoleIds.add(role.getId().toString());
            roles.add(role);
        }
        model.addAttribute(user);
        //待选角色列表
        model.addAttribute("roles",roles);
        //已勾选角色ID
        model.addAttribute("checkRoleId",String.join(",",checkRoleIds));
        return "user/userEdit";
    }

    /**
     * 用户详情页
     */
    @RequestMapping("/userDetail")
    public String userDetail(@RequestParam String userId, Model model){
        User user = userService.getUserDetail(userId);
        model.addAttribute(user);
        return "user/userDetail";
    }

    /**
     * 修改用户
     */
    @PreAuthorize("hasAuthority('user:alt')")
    @ResponseBody
    @RequestMapping("/userSave")
    public ModelMap saveUser(@Valid User user){
        try {
            userService.altUser(user);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 删除用户
     */
    @PreAuthorize("hasAuthority('user:del')")
    @ResponseBody
    @RequestMapping("/userDel")
    public ModelMap delUser(@RequestParam String userId){
        try {
            userService.delUser(userId);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 分类列表页
     */
    @PreAuthorize("hasAuthority('clazz:view')")
    @RequestMapping("/classify")
    public String classifyList(){
        return "classify/classifyList";
    }

    /**
     * 获取分类列表
     */
    @ResponseBody
    @RequestMapping("/classifyList")
    public ModelMap getClassifyList(@RequestParam(value="page",defaultValue="1") Integer page,
                                    @RequestParam(value="size",defaultValue="10") Integer size, Classify classify){
        Page<Classify> classifyList = classifyService.getClassifyList(PageRequest.of(page - 1, size), classify);
        return RestUtil.Success(classifyList.getTotalElements(),classifyList.getContent());
    }

    /**
     * 修改分类页
     */
    @RequestMapping("/classifyAlt")
    public String altClassify(@RequestParam(required = false)Long id,Model model){
        Classify classify = new Classify();
        if(id!=null){
            classify = classifyService.getClassifyDetail(id);
        }
        model.addAttribute(classify);
        return "classify/classifyEdit";
    }

    /**
     * 保存分类
     */
    @PreAuthorize("hasAnyAuthority('clazz:add','clazz:alt')")
    @ResponseBody
    @PostMapping("/saveClassify")
    public ModelMap saveClassify(@Valid Classify classify){
        try {
            classifyService.saveClassify(classify);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 删除分类
     */
    @PreAuthorize("hasAuthority('clazz:del')")
    @ResponseBody
    @GetMapping("/classifyDel/{id}")
    public ModelMap delClassify(@PathVariable("id")Long id){
        try {
            classifyService.deleteClassify(id);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 文章列表页
     */
    @PreAuthorize("hasAuthority('story:view')")
    @RequestMapping("/essay")
    public String essayList(){
        return "essay/essayList";
    }

    /**
     * 公告列表页
     */
    @PreAuthorize("hasAuthority('not:view')")
    @RequestMapping("/notice")
    public String noticeList(){
        return "notice/noticeList";
    }

    /**
     * 获取公告列表
     */
    @ResponseBody
    @GetMapping("/noticeList")
    public ModelMap getNoticeList(@RequestParam(value="page",defaultValue="1") Integer page,
                                    @RequestParam(value="size",defaultValue="10") Integer size, Notice notice){
        Page<Notice> notices =noticeService.getNoticeList(PageRequest.of(page - 1, size), notice);
        return RestUtil.Success(notices.getTotalElements(),notices.getContent());
    }

    /**
     * 修改公告页
     */
    @RequestMapping("/noticeAlt")
    public String altNotice(@RequestParam(required = false)Long id,Model model){
        Notice notice = new Notice();
        if(id!=null){
            notice = noticeService.getNoticeDetail(id);
        }
        model.addAttribute(notice);
        return "notice/noticeEdit";
    }

    /**
     * 保存公告
     */
    @PreAuthorize("hasAnyAuthority('not:add','not:alt')")
    @ResponseBody
    @PostMapping("/saveNotice")
    public ModelMap saveNotice(@Valid Notice notice){
        try {
            noticeService.addOrUpdateNotice(notice);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 删除公告
     */
    @PreAuthorize("hasAuthority('not:del')")
    @ResponseBody
    @GetMapping("/noticeDel/{id}")
    public ModelMap delNotice(@PathVariable("id")Long id){
        try {
            noticeService.deleteNotice(id);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 轮播图页
     */
    @PreAuthorize("hasAuthority('sel:view')")
    @RequestMapping("/carousel")
    public String carouselList(){
        return "carousel/carouselList";
    }

    /**
     * 获取轮播图列表
     */
    @ResponseBody
    @GetMapping("/carouselList")
    public ModelMap carouselList(@RequestParam(value="page",defaultValue="1") Integer page,
                              @RequestParam(value="size",defaultValue="10") Integer size,String name){
        Page<CarouselCategory> carouselCategory = carouselService.getCarouselList(name,PageRequest.of(page - 1, size));
        return RestUtil.Success(carouselCategory.getTotalElements(),carouselCategory.getContent());
    }

    /**
     * 修改轮播分类页
     */
    @RequestMapping("/carouselCategoryAlt")
    public String altCarouselCategory(@RequestParam(required = false)String id,Model model){
        CarouselCategory carouselCategory = new CarouselCategory();
        if(id!=null){
            carouselCategory = carouselService.getCarouselDetail(id);
        }
        model.addAttribute(carouselCategory);
        return "carousel/carouselCategoryEdit";
    }

    /**
     * 修改轮播图页
     */
    @RequestMapping("/carouselAlt")
    public String altCarousel(@RequestParam String id,Model model){
        CarouselCategory carouselCategory = carouselService.getCarouselDetail(id);
        model.addAttribute(carouselCategory);
        return "carousel/carouselEdit";
    }

    /**
     * 保存轮播图
     */
    @PreAuthorize("hasAnyAuthority('sel:add','sel:alt')")
    @ResponseBody
    @PostMapping("/saveCarousel")
    public ModelMap saveCarousel(@Valid CarouselCategory carouselCategory){
        try {
            carouselService.addOrUpdateCarousel(carouselCategory);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 添加轮播图
     */
    @PreAuthorize("hasAnyAuthority('sel:add','sel:alt')")
    @ResponseBody
    @RequestMapping("/addCarousel")
    public ModelMap addCarousel(@RequestParam("file") MultipartFile file,@RequestParam("id")String id){
        if(file.isEmpty()){
            return RestUtil.Error(222,"文件为空");
        }
        String path;
        try {
            path = UploadUtil.uploadFileByAli(file,"img");
            carouselService.addCarousel(id,path);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.Error(RestCode.FILE_UPLOAD_ERR);
        }
        return RestUtil.Success();
    }

    /**
     * 删除轮播分类
     */
    @PreAuthorize("hasAuthority('sel:del')")
    @ResponseBody
    @GetMapping("/carouselDel/{id}")
    public ModelMap delCarouselCategory(@PathVariable("id")String id){
        try {
            carouselService.deleteCarouselCategory(id);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 删除轮播图
     */
    @PreAuthorize("hasAuthority('sel:del')")
    @ResponseBody
    @GetMapping("/carouselDel")
    public ModelMap delCarousel(@RequestParam("id")String id,@RequestParam("url")String url){
        try {
            carouselService.deleteCarousel(id,url);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

}
