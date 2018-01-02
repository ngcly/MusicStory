package com.cn.controller;

import com.cn.config.ManagerDetail;
import com.cn.dao.PermissionRepository;
import com.cn.entity.Permission;
import com.cn.entity.Role;
import com.cn.util.MenuUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 后台首页控制器
 *
 * @author chen
 * @date 2018-01-02 17:26
 */
@Controller
public class HomeController {
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    DefaultKaptcha defaultKaptcha;

    /**
     * 默认根目录为spring data rest 配置文件根目录
     * 这里必须得配置个 / 不然所有静态文件全部不起作用
     * 只有security才这样 shiro正常 原因尚不知
     */
    @RequestMapping("/")
    public String index(Principal principal, Model model) {
        ManagerDetail managerDetail = (ManagerDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> roleList = managerDetail.getRoleList();
        List<Permission> menuList = new ArrayList<>();
        if (!"admin".equals(principal.getName())) {
            roleList.forEach(role -> menuList.addAll(role.getPermissions()));
        } else {
            menuList.addAll(permissionRepository.findMenuList("1"));
        }
        model.addAttribute("menuList", MenuUtil.makeTreeList(menuList));
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
}
