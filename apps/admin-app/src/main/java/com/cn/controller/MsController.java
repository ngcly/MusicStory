package com.cn.controller;

import com.cn.user.UserService;
import com.cn.security.RoleService;
import com.cn.content.ClassifyService;
import com.cn.notification.NoticeService;
import com.cn.content.CarouselService;
import com.cn.content.EssayService;
import com.cn.entity.*;
import com.cn.persistence.mapper.UserMapper;
import com.cn.enums.UserTypeEnum;
import com.cn.service.StorageService;
import com.cn.util.Result;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
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
    private final StorageService storageService;

    /**
     * 用户列表页
     */
    @GetMapping("/user.html")
    public String userList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            User user,
            @RequestHeader(value = "HX-Request", required = false) boolean hxRequest,
            Model model) {
        
        Page<com.cn.user.domain.User> userList = userService.getUserList(PageRequest.of(page - 1, size), UserMapper.toDomain(user));
        Page<User> entityUserList = userList.map(UserMapper::toEntity);
        
        model.addAttribute("userPage", entityUserList);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("userParam", user);
        
        if (hxRequest) {
            return "user/userList :: userTable";
        }
        return "user/userList";
    }

    @GetMapping("/user/edit.html")
    public String userEdit(@RequestParam Long userId, Model model) {
        User user = UserMapper.toEntity(userService.getUserDetail(userId));
        var roles = roleService.getAvailableRoles(UserTypeEnum.USER);
        String checkRoleIds = "";
        if (user.getRoleList() != null) {
            checkRoleIds = user.getRoleList().stream().map(role ->
                    role.getId().toString()).collect(Collectors.joining(","));
        }
        model.addAttribute(user);
        model.addAttribute("roles", roles);
        model.addAttribute("checkRoleId", checkRoleIds);
        return "user/userEdit :: userEditFragment";
    }

    @GetMapping("/user/{userId}")
    public String userDetail(@PathVariable Long userId, Model model) {
        User user = UserMapper.toEntity(userService.getUserDetail(userId));
        model.addAttribute(user);
        return "user/userDetail :: userDetailFragment";
    }

    @PutMapping("/user")
    public ResponseEntity<Void> saveUser(User user, HttpServletResponse response) {
        userService.altUser(UserMapper.toDomain(user));
        response.setHeader("HX-Trigger", "userListChanged");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> delUser(@RequestParam Long userId) {
        userService.delUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/classify.html")
    public String classifyList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Classify classify,
            @RequestHeader(value = "HX-Request", required = false) boolean hxRequest,
            Model model) {
        
        Page<Classify> classifyList = classifyService.getClassifyList(PageRequest.of(page - 1, size), classify);
        
        model.addAttribute("classifyPage", classifyList);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("classifyParam", classify);
        
        if (hxRequest) {
            return "classify/classifyList :: classifyTable";
        }
        return "classify/classifyList";
    }
 
    @GetMapping("/classify/edit.html")
    public String altClassify(@RequestParam(required = false) Long id, Model model) {
        Classify classify = new Classify();
        if (id != null) {
            classify = classifyService.getClassifyDetail(id);
        }
        model.addAttribute(classify);
        return "classify/classifyEdit :: classifyEditFragment";
    }
 
    @RequestMapping(value = "/classify", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Void> addClassify(@Valid Classify classify, HttpServletResponse response) {
        classifyService.saveClassify(classify);
        response.setHeader("HX-Trigger", "classifyListChanged");
        return ResponseEntity.ok().build();
    }
 
    @DeleteMapping("/classify")
    public ResponseEntity<Void> delClassify(@RequestParam("id") Long id) {
        classifyService.deleteClassify(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/essay.html")
    public String essayList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Essay essay,
            @RequestHeader(value = "HX-Request", required = false) boolean hxRequest,
            Model model) {
        
        Page<Essay> essayList = essayService.getEssayList(PageRequest.of(page - 1, size, Sort.by("createdAt").descending()), essay);
        
        model.addAttribute("essayPage", essayList);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("essayParam", essay);
        model.addAttribute("classifyList", classifyService.getClassifyList());
        
        if (hxRequest) {
            return "essay/essayList :: essayTable";
        }
        return "essay/essayList";
    }
 
    @GetMapping("/essay/edit.html")
    public String essayAlt(@RequestParam Long id, Model model) {
        Essay essay = essayService.getEssayDetail(id);
        model.addAttribute(essay);
        return "essay/essayEdit :: essayEditFragment";
    }
 
    @PostMapping("/essay")
    public ResponseEntity<Void> essaySave(@Valid Essay essay, HttpServletResponse response) {
        essayService.altEssayState(essay);
        response.setHeader("HX-Trigger", "essayListChanged");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notice.html")
    public String noticeList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Notice notice,
            @RequestHeader(value = "HX-Request", required = false) boolean hxRequest,
            Model model) {
        
        Page<Notice> notices = noticeService.getNoticeList(PageRequest.of(page - 1, size), notice);
        
        model.addAttribute("noticePage", notices);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("noticeParam", notice);
        
        if (hxRequest) {
            return "notice/noticeList :: noticeTable";
        }
        return "notice/noticeList";
    }
 
    @GetMapping("/notice/edit.html")
    public String altNotice(@RequestParam(required = false) Long id, Model model) {
        Notice notice = new Notice();
        if (id != null) {
            notice = noticeService.getNoticeDetail(id);
        }
        model.addAttribute(notice);
        return "notice/noticeEdit :: noticeEditFragment";
    }
 
    @RequestMapping(value = "/notice", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Void> addOrUpdateNotice(@Valid Notice notice, HttpServletResponse response) {
        noticeService.addOrUpdateNotice(notice);
        response.setHeader("HX-Trigger", "noticeListChanged");
        return ResponseEntity.ok().build();
    }
 
    @DeleteMapping("/notice")
    public ResponseEntity<Void> delNotice(@RequestParam Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/carousel.html")
    public String carouselList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestHeader(value = "HX-Request", required = false) boolean hxRequest,
            Model model) {
        
        Page<CarouselCategory> carouselCategory = carouselService.getCarouselList(name, PageRequest.of(page - 1, size));
        
        model.addAttribute("carouselPage", carouselCategory);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("nameParam", name);
        
        if (hxRequest) {
            return "carousel/carouselList :: carouselTable";
        }
        return "carousel/carouselList";
    }
 
    @GetMapping("/carousel/category/edit.html")
    public String altCarouselCategory(@RequestParam(required = false) Long id, Model model) {
        CarouselCategory carouselCategory = new CarouselCategory();
        if (id != null) {
            carouselCategory = carouselService.getCarouselDetail(id);
        }
        model.addAttribute(carouselCategory);
        return "carousel/carouselCategoryEdit :: carouselCategoryEditFragment";
    }
 
    @GetMapping("/carousel/edit.html")
    public String altCarousel(@RequestParam Long id, Model model) {
        CarouselCategory carouselCategory = carouselService.getCarouselDetail(id);
        model.addAttribute(carouselCategory);
        return "carousel/carouselEdit :: carouselEditFragment";
    }
 
    @PostMapping("/carousel/category")
    public ResponseEntity<Void> saveCarousel(@Valid CarouselCategory carouselCategory, HttpServletResponse response) {
        carouselService.addOrUpdateCarousel(carouselCategory);
        response.setHeader("HX-Trigger", "carouselListChanged");
        return ResponseEntity.ok().build();
    }
 
    @PostMapping("/carousel")
    public ResponseEntity<Result<Void>> addCarousel(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id, HttpServletResponse response) {
        if (file.isEmpty()) {
            return ResponseEntity.ok(Result.failure(222, "文件为空"));
        }
        String path = storageService.uploadFile(file, "img");
        carouselService.addCarousel(id, path);
        response.setHeader("HX-Trigger", "carouselImagesChanged");
        return ResponseEntity.ok(Result.success());
    }
 
    @DeleteMapping("/carousel/category")
    public ResponseEntity<Void> delCarouselCategory(@RequestParam Long id) {
        carouselService.deleteCarouselCategory(id);
        return ResponseEntity.ok().build();
    }
 
    @DeleteMapping("/carousel")
    public ResponseEntity<Void> delCarousel(@RequestParam("id") Long id, @RequestParam("url") String url, HttpServletResponse response) {
        carouselService.deleteCarousel(id, url);
        response.setHeader("HX-Trigger", "carouselImagesChanged");
        return ResponseEntity.ok().build();
    }

}
