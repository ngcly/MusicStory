package com.cn.controller;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cn.LogService;
import com.cn.ManagerService;
import com.cn.RoleService;
import com.cn.pojo.MenuDTO;
import com.cn.pojo.RestCode;
import com.cn.entity.LoginLog;
import com.cn.entity.Manager;
import com.cn.entity.Permission;
import com.cn.entity.Role;
import com.cn.util.RestUtil;
import com.cn.util.UploadUtil;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

/**
 * 系统设置 控制类
 *
 * @author chen
 * @date 2018-01-02 18:25
 */
@Controller
@RequestMapping("/sys")
@AllArgsConstructor
public class SystemController {
    private final ManagerService managerService;
    private final RoleService roleService;
    private final LogService logService;

    private static final Log log = LogFactory.get();

    /*
      下面两个底层实现一样  唯一区别就是hasRole默认添加 ROLE_ 前缀
      @PreAuthorize("hasAuthority('ROLE_admin')")
      @PreAuthorize("hasRole('admin')") 方法调用前判断是否有权限
      @PreAuthorize("hasPermission('','sys:user')") 判断自定义权限标识符
      @PostAuthorize("returnObject.id%2==0") 方法调用完后判断 若为false则无权限  基本用不上
      @PostFilter("filterObject.id%2==0") 对返回结果进行过滤  filterObject内置为返回对象
      @PreFilter(filterTarget="ids", value="filterObject%2==0") 对方法参数进行过滤 如有多参则指定参数 ids为其中一个参数
     */

    /**
     * 管理员列表页
     */
    @PreAuthorize("hasAuthority('sys:admin')")
    @RequestMapping("/adminList")
    public String managerList(@PageableDefault(sort = { "createdAt" }, direction = Sort.Direction.DESC)
                                      Pageable pageable, Model model, @Valid Manager manager){
        Page<Manager> managerList = managerService.getManagersList(pageable,manager);
        model.addAttribute("managerList",managerList);
        model.addAttribute("manger",manager);
        return "manager/managerList";
    }

    /**
     * 管理员详情页
     */
    @RequestMapping("/adminView")
    public String managerDetail(@RequestParam Long mangerId, Model model){
        Manager manager = managerService.getManagerById(mangerId);
        model.addAttribute("manager",manager);
        return "manager/managerDetail";
    }

    /**
     * 管理员编辑页
     */
    @RequestMapping("/adminEdit")
    public String altManager(@RequestParam(required = false)Long managerId, Principal principal,Model model){
        //最好是从当前授权信息里面提出角色列表来
        Authentication authentication = (Authentication) principal;
        Manager managerDetail = (Manager) authentication.getPrincipal();
        Set<Role> roles = managerDetail.getRoleList();
        if("admin".equals(managerDetail.getUsername())){
            roles = roleService.getAvailableRoles(Role.ROLE_TYPE_MANAGER);
        }
        List<String> optRole = new ArrayList<>();
        roles.forEach(role -> optRole.add(role.getId().toString()));
        Manager manager = new Manager();
        ArrayList<String> checkRoleIds = new ArrayList<>();
        if(managerId!=null){
            manager = managerService.getManagerById(managerId);
            for(Role role:manager.getRoleList()){
                checkRoleIds.add(role.getId().toString());
                roles.add(role);
            }
        }
        model.addAttribute("currentId",managerDetail.getId());
        model.addAttribute("manager",manager);
        //待选角色列表
        model.addAttribute("roles",roles);
        //已勾选角色ID
        model.addAttribute("checkRoleId",String.join(",",checkRoleIds));
        //可授权角色ID
        model.addAttribute("optionRoles",String.join(",",optRole));
        return "manager/managerEdit";
    }

    /**
     * 新增或修改管理员信息
     */
    @ResponseBody
    @RequestMapping("/adminSave")
    public ModelMap register(@Valid Manager manager){
        Manager managerDetail = (Manager) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        managerService.saveManager(managerDetail,manager);
        return RestUtil.success();
    }

    /**
     * 删除管理员
     */
    @PreAuthorize("hasAuthority('admin:del')")
    @ResponseBody
    @RequestMapping("/adminDel")
    public ModelMap delManager(@RequestParam Long managerId){
        Manager managerDetail = (Manager) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        if(managerDetail.getId().equals(managerId) || Long.valueOf(1).equals(managerId)){
            return RestUtil.failure(333,"禁止删除自己和admin");
        }
        managerService.delManager(managerId);
        return RestUtil.success();
    }

    /**
     * 修改密码页面
     * @return String
     */
    @RequestMapping("/altPwd")
    public String altPwd(){
        return "manager/updatePwd";
    }

    /**
     * 修改密码
     */
    @RequestMapping("/updatePwd")
    @ResponseBody
    public ModelMap updatePassword(@RequestParam String oldPassword,@RequestParam String password){
        Manager managerDetail = (Manager) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        managerService.updatePassword(managerDetail.getId(),oldPassword,password);
        return RestUtil.success();
    }

    /**
     * 重置管理员密码
     * @param managerId 管理员id
     */
    @PreAuthorize("hasAuthority('admin:reset')")
    @ResponseBody
    @RequestMapping("/resetPwd")
    public ModelMap resetPassword(@RequestParam Long managerId){
        managerService.updatePassword(managerId,"123456");
        return RestUtil.success();
    }

    /**
     * 角色列表页
     */
    @PreAuthorize("hasAuthority('role:view')")
    @RequestMapping("/roleList")
    public String roleList(@PageableDefault(sort = { "roleName" }, direction = Sort.Direction.DESC)
                                   Pageable pageable,@Valid Role role,Model model){
        Page<Role> roleList = roleService.getRoleList(pageable,role);
        model.addAttribute("roleList",roleList);
        model.addAttribute("roleRt",role);
        return "role/roleList";
    }

    /**
     * 新增或修改角色页面
     */
    @PreAuthorize("hasAnyAuthority('role:add','role:alt')")
    @RequestMapping("/roleEdit")
    public String roleEdit(@RequestParam(required = false) Long roleId,Model model){
        Role role = new Role();
        if(roleId!=null){
            role = roleService.findRole(roleId);
        }
        model.addAttribute("role",role);
        return "role/roleEdit";
    }

    /**
     * 保存角色
     */
    @PreAuthorize("hasAnyAuthority('role:add','role:alt')")
    @RequestMapping("/roleSave")
    @ResponseBody
    public ModelMap saveRole(@Valid Role role){
        try {
            roleService.saveRole(role);
        }catch (DataIntegrityViolationException e){
            return RestUtil.failure(333,"唯一标识符重复");
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.failure(RestCode.SERVER_ERROR);
        }
        return RestUtil.success();
    }

    /**
     * 角色授权页面
     */
    @PreAuthorize("hasAnyAuthority('role:grant','role:view')")
    @RequestMapping("/grant")
    public String grantForm(@RequestParam long roleId,@RequestParam String type, Model model) {
        Set<MenuDTO> menuSet = roleService.getMenuListWithChecked(roleId);
        String menuList = JSONUtil.toJsonStr(menuSet);
        model.addAttribute("type",type);
        model.addAttribute("roleId", roleId);
        model.addAttribute("menuList",menuList);
        return "role/grant";
    }

    /**
     * 保存授权
     * @param roleId 角色id
     * @param menuIds 菜单id列表
     * @return ModelMap
     */
    @PreAuthorize("hasAuthority('role:grant')")
    @PostMapping("/saveGrant")
    @ResponseBody
    public ModelMap saveGrant(Long roleId, String menuIds){
        roleService.saveGrant(roleId,menuIds);
        return RestUtil.success();
    }

    /**
     * 是否停用角色
     */
    @PreAuthorize("hasAuthority('role:alt')")
    @RequestMapping("/togAvailable")
    @ResponseBody
    public ModelMap altRole(@RequestParam long roleId){
        roleService.altAvailable(roleId);
        return RestUtil.success();
    }

    /**
     * 删除角色
     */
    @PreAuthorize("hasAuthority('role:del')")
    @RequestMapping("/roleDel")
    @ResponseBody
    public ModelMap delRole(@RequestParam long roleId){
        roleService.delRole(roleId);
        return RestUtil.success();
    }

    /**
     * 菜单列表页
     */
    @PreAuthorize("hasAuthority('menu:view')")
    @RequestMapping("/menuList")
    public String menuList(Model model){
        List<Permission> menuList = roleService.getMenuList();
        model.addAttribute("menuList",menuList);
        return "menu/menuList";
    }

    /**
     * 新增或修改菜单页
     */
    @PreAuthorize("hasAnyAuthority('menu:add','menu:alt')")
    @RequestMapping("/menuEdit")
    public String menuEdit(@RequestParam(required = false)Long menuId,@RequestParam(required = false)Long parentId,Model model){
        Permission permission = new Permission();
        permission.setSort(0);
        String parentName="";
        if(parentId!=null){
            parentName = roleService.getPermissionById(parentId).getName();
            permission.setParentId(parentId);
        }
        if(menuId!=null){
            permission = roleService.getPermissionById(menuId);
            if(!MenuDTO.rootId.equals(permission.getParentId())){
                parentName = roleService.getPermissionById(permission.getParentId()).getName();
            }else{
                parentName = "顶层菜单";
            }
        }
        model.addAttribute("menu",permission);
        model.addAttribute("parentName",parentName);
        return "menu/menuEdit";
    }

    /**
     * 保存菜单
     */
    @PreAuthorize("hasAnyAuthority('menu:add','menu:alt')")
    @RequestMapping("/menuSave")
    @ResponseBody
    public ModelMap saveMenu(@Valid Permission permission){
        roleService.saveMenu(permission);
        return RestUtil.success();
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("hasAuthority('menu:del')")
    @RequestMapping("/menuDel")
    @ResponseBody
    public ModelMap delMenu(@RequestParam long menuId){
        roleService.delMenu(menuId);
        return RestUtil.success();
    }

    /**
     * 登录日志列表页
     */
    @PreAuthorize("hasAuthority('sys:log')")
    @RequestMapping("/loginLogs")
    public String loginLogList(@PageableDefault(sort = { "loginTime" }, direction = Sort.Direction.DESC)
                                       Pageable pageable, @Valid LoginLog loginLog, Model model){
        Page<LoginLog> logs = logService.getLoginLogList(pageable,loginLog);
        model.addAttribute("logList",logs);
        model.addAttribute("log",loginLog);
        return "system/logList";
    }

    /**
     * 头像上传
     * @param file 文件
     * @return ModelMap
     */
    @RequestMapping("/upload")
    @ResponseBody
    public ModelMap uploadAvatar(@RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            return RestUtil.failure(222,"文件为空");
        }
        try {
            String path = UploadUtil.uploadFileByAli(file,"avatar");
            return RestUtil.success(path);
        } catch (Exception e) {
            log.error(e,"上传文件失败");
            return RestUtil.failure(RestCode.FILE_UPLOAD_ERR);
        }
    }
}
