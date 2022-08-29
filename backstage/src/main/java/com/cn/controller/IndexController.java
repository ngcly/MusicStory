package com.cn.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import com.cn.ManagerService;
import com.cn.RoleService;
import com.cn.entity.Manager;
import com.cn.entity.Permission;
import com.cn.entity.Role;
import com.cn.pojo.MenuDTO;
import com.cn.pojo.CaptchaInfo;
import com.cn.util.MenuUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 后台首页控制器
 *
 * @author ngcly
 * @since 2018-01-02 17:26
 */
@Controller
@AllArgsConstructor
public class IndexController {
    private final ManagerService managerService;
    private final RoleService roleService;

    /**
     * 首页控制台
     */
    @RequestMapping("/")
    public String index(@AuthenticationPrincipal Manager manager, Model model) {
        boolean init = false;
        Collection<Role> roleList;
        if ("administrator".equals(manager.getUsername())) {
            roleList = managerService.getAdministrator().getRoleList();
        } else {
            Manager dbManager = managerService.getManagerById(manager.getId());
            roleList = dbManager.getRoleList();
            if (dbManager.getState() == Manager.STATE_INITIALIZE) {
                init = true;
                dbManager.setState(Manager.STATE_NORMAL);
                managerService.updateManager(dbManager);
            }
        }
        List<MenuDTO> menuList = roleList.parallelStream()
                .map(Role::getPermissions)
                .flatMap(Collection::parallelStream)
                .filter(permission -> Permission.RESOURCE_MENU.equals(permission.getResourceType()))
                .distinct()
                .map(permission -> (MenuDTO) permission)
                .toList();

        model.addAttribute("init", init);
        model.addAttribute("manager", manager);
        model.addAttribute("menuList", MenuUtil.makeMenuToTree(menuList));
        return "index";
    }

    /**
     * 登录页面
     *
     * @return login.html
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 验证码
     *
     * @param request  请求对象
     */
    @GetMapping("/captcha")
    public StreamingResponseBody generateCaptcha(HttpServletRequest request) {
        ICaptcha captcha = CaptchaUtil.createGifCaptcha(116, 36, 4);
        CaptchaInfo captchaInfo = new CaptchaInfo(captcha.getCode(), 60);
        request.getSession().setAttribute("captcha", captchaInfo);
        return captcha::write;
    }

    /**
     * 主页
     */
    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * 云中遨游
     */
    @RequestMapping("/clouds")
    public String clouds() {
        return "other/clouds";
    }

}
