package com.cn.controller;

import com.cn.ManagerService;
import com.cn.RoleService;
import com.cn.config.ManagerDetail;
import com.cn.dao.PermissionRepository;
import com.cn.entity.Manager;
import com.cn.entity.Permission;
import com.cn.entity.Role;
import com.cn.util.MenuUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.*;

/**
 * 后台首页控制器
 *
 * @author chen
 * @date 2018-01-02 17:26
 */
@Controller
public class IndexController {
    @Autowired
    RoleService roleService;
    @Autowired
    DefaultKaptcha defaultKaptcha;
    @Autowired
    ManagerService managerService;

    /**
     * 若加载了data rest包 则默认根目录为 所有接口link
     * 这里必须得配置个 / 不然所有静态文件全部不起作用
     * 只有security才这样 shiro正常 原因未知
     */
    @RequestMapping("/")
    public String index(Principal principal, Model model) {
        Authentication authentication = (Authentication) principal;
        ManagerDetail managerDetail = (ManagerDetail) authentication.getPrincipal();
        Set<Role> roleList = managerDetail.getRoleList();
        boolean init = false;
        List<Permission> menuList = new ArrayList<>();
        if (!"admin".equals(principal.getName())) {
            roleList.forEach(role -> menuList.addAll(role.getPermissions()));
        } else {
            //管理员拥有最高权限
            List<Role> roles = roleService.getAllRole();
            List<Permission> permissions = roleService.getPermissionList();
            // 得到当前的认证信息
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            //  生成当前的所有授权
            Collection<GrantedAuthority> updatedAuthorities = new HashSet<>(auth.getAuthorities());
            // 添加 授权
            roles.forEach(role -> updatedAuthorities.add(new SimpleGrantedAuthority(role.getRoleCode())));
            permissions.forEach(permission -> updatedAuthorities.add(new SimpleGrantedAuthority(permission.getPurview())));
            // 生成新的认证信息
            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
            // 重置认证信息
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            menuList.addAll(permissions);
        }
        if(managerDetail.getState()==0){
            init = true;
            Manager manager = managerService.getManagerById(managerDetail.getId());
            manager.setState((byte) 1);
            managerService.updateManager(manager);
        }
        model.addAttribute("init",init);
        model.addAttribute("manager",managerService.getManagerById(managerDetail.getId()));
        model.addAttribute("menuList", MenuUtil.makeTreeList(menuList));
        return "index";
    }

    /**
     * 登录页面
     *
     * @return login.html
     */
    @RequestMapping("/login")
    public String login(@RequestParam(required = false)String error,Model model) {
        model.addAttribute("error",error);
        return "login";
    }

    /**
     * kaptcha 验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/kaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception{
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            request.getSession().setAttribute("validateCode", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    /**
     * 主页
     */
    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    /**
     * 云中遨游
     */
    @RequestMapping("/clouds")
    public String clouds(){
        return "other/clouds";
    }

}
