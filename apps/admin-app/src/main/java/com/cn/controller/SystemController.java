package com.cn.controller;

import com.cn.security.LogService;
import com.cn.security.ManagerService;
import com.cn.security.RoleService;
import com.cn.entity.*;
import com.cn.model.MenuDTO;
import com.cn.service.StorageService;
import com.cn.util.JacksonUtil;
import com.cn.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统设置 控制类
 *
 * @author chen
 * @date 2018-01-02 18:25
 */
@Controller
@RequestMapping(value = "/sys", name = "系统设置")
@AllArgsConstructor
public class SystemController {
    private final ManagerService managerService;
    private final RoleService roleService;
    private final LogService logService;
    private final StorageService storageService;

    /**
     * 权限注解说明
     * 下面两个权限注解底层实现一样  唯一区别就是hasRole默认添加 ROLE_ 前缀
     *
     * @PreAuthorize("hasAuthority('ROLE_admin')")
     * @PreAuthorize("hasRole('admin')") 方法调用前判断是否有权限
     * @PreAuthorize("hasPermission('sys','created')") 自定义判断权限标识符
     * @PostAuthorize("returnObject.id%2==0") 方法调用完后判断 若为false则无权限  基本用不上
     * @PostFilter("filterObject.id%2==0") 对返回结果进行过滤  filterObject内置为返回对象
     * @PreFilter(filterTarget="ids", value="filterObject%2==0") 对方法参数进行过滤 如有多参则指定参数 ids为其中一个参数
     * 上述注解方式属于静态鉴权 由于本项目采用了动态鉴权模式:@link MyAuthorizationManager .所以上述注解并未使用到
     * <p>
     * 管理员列表页
     */
    @GetMapping("/manager.html")
    public String managerList(
            @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable,
            @Valid Manager manager,
            @RequestHeader(value = "HX-Request", required = false) boolean hxRequest,
            Model model) {
        
        Page<Manager> managerList = managerService.getManagersList(
                pageable.withPage(Math.max(0, pageable.getPageNumber() - 1)), manager);
        
        model.addAttribute("managerPage", managerList);
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("managerParam", manager);
        
        if (hxRequest) {
            return "manager/managerList :: managerTable";
        }
        return "manager/managerList";
    }

    /**
     * 管理员详情页
     */
    @GetMapping("/manager/{managerId}")
    public String managerDetail(@PathVariable Long managerId, Model model) {
        Manager manager = managerService.getManagerById(managerId);
        model.addAttribute("manager", manager);
        return "manager/managerDetail :: managerDetailFragment";
    }

    /**
     * 管理员编辑页
     */
    @GetMapping("/manager/edit.html")
    public String altManager(@AuthenticationPrincipal Manager managerDetail,
                             @RequestParam(required = false) Long managerId, Model model) {
        //最好是从当前授权信息里面提出角色列表来
        Set<Role> currentManagerRoles = managerDetail.getRoleList();
        String optRole =
                currentManagerRoles.stream().map(role -> role.getId().toString()).collect(Collectors.joining(","));

        Manager manager;
        String checkRoleIds;
        Set<Role> beSelectedRoles;
        if (managerId != null) {
            manager = managerService.getManagerById(managerId);
            checkRoleIds = manager.getRoleList().stream().map(role ->
                    role.getId().toString()).collect(Collectors.joining(","));
            beSelectedRoles =
                    Stream.concat(currentManagerRoles.stream(), manager.getRoleList().stream()).collect(Collectors.toSet());
        } else {
            manager = new Manager();
            checkRoleIds = "";
            beSelectedRoles = Collections.unmodifiableSet(currentManagerRoles);
        }
        model.addAttribute("currentId", managerDetail.getId());
        model.addAttribute("manager", manager);
        //待选角色列表
        model.addAttribute("roles", beSelectedRoles);
        //已勾选角色ID
        model.addAttribute("checkRoleId", checkRoleIds);
        //可授权角色ID
        model.addAttribute("optionRoles", optRole);
        return "manager/managerEdit :: managerEditFragment";
    }

    /**
     * 新增管理员信息
     */
    @PostMapping("/manager")
    public ResponseEntity<String> addManager(@AuthenticationPrincipal Manager curManager, @Valid Manager manager, HttpServletResponse response) {
        managerService.saveManager(curManager, manager);
        response.setHeader("HX-Trigger", "managerListChanged");
        return ResponseEntity.ok().build();
    }

    /**
     * 修改管理员信息
     */
    @PutMapping("/manager")
    public ResponseEntity<String> updateManager(@AuthenticationPrincipal Manager curManager, @Valid Manager manager, HttpServletResponse response) {
        if (Manager.ADMIN.equals(curManager.getUsername())) {
            return ResponseEntity.badRequest().body("当前用户属于内置管理员，不支持信息修改");
        }
        managerService.saveManager(curManager, manager);
        response.setHeader("HX-Trigger", "managerListChanged");
        return ResponseEntity.ok().build();
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/manager")
    public ResponseEntity<String> deleteManager(@AuthenticationPrincipal Manager curManager, @RequestParam Long managerId) {
        if (curManager.getId().equals(managerId)) {
            return ResponseEntity.badRequest().body("禁止删除自己");
        }
        managerService.delManager(managerId);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改密码页面
     *
     * @return String
     */
    @GetMapping("/manager/pwd.html")
    public String altPwd() {
        return "manager/updatePwd :: updatePwdFragment";
    }

    /**
     * 修改密码
     */
    @PutMapping("/manager/pwd")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal Manager manager, @RequestParam String oldPassword,
                                       @RequestParam String password, HttpServletResponse response) {
        if ("administrator".equals(manager.getUsername())) {
            return ResponseEntity.badRequest().body("当前用户属于内置管理员,不支持密码修改");
        }
        managerService.updatePassword(manager.getId(), oldPassword, password);
        response.setHeader("HX-Trigger", "pwdUpdated");
        return ResponseEntity.ok().build();
    }

    /**
     * 重置管理员密码
     *
     * @param managerId 管理员id
     */
    @PostMapping("/manager/pwd")
    public ResponseEntity<Void> resetPassword(@RequestParam Long managerId, HttpServletResponse response) {
        managerService.updatePassword(managerId, "123456");
        response.setHeader("HX-Trigger", "managerListChanged");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/role.html")
    public String roleList(
            @PageableDefault(sort = {"roleName"}, direction = Sort.Direction.DESC) Pageable pageable,
            @Valid Role role,
            @RequestHeader(value = "HX-Request", required = false) boolean hxRequest,
            Model model) {
        
        // Convert to 0-based page for spring data
        Page<Role> roleList = roleService.getRoleList(
                pageable.withPage(Math.max(0, pageable.getPageNumber() - 1)), role);
        
        model.addAttribute("rolePage", roleList);
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("roleParam", role);
        
        if (hxRequest) {
            return "role/roleList :: roleTable";
        }
        return "role/roleList";
    }
 
    @GetMapping("/role/edit.html")
    public String roleEdit(@RequestParam(required = false) Long roleId, Model model) {
        Role role = new Role();
        if (roleId != null) {
            role = roleService.findRole(roleId);
        }
        model.addAttribute("role", role);
        return "role/roleEdit :: roleEditFragment";
    }
 
    @RequestMapping(value = "/role", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Void> addRole(@Valid Role role, HttpServletResponse response) {
        roleService.saveRole(role);
        response.setHeader("HX-Trigger", "roleListChanged");
        return ResponseEntity.ok().build();
    }
 
    @GetMapping("/role/grant.html")
    public String grantForm(@RequestParam long roleId, @RequestParam String type, Model model) throws JsonProcessingException {
        Collection<MenuDTO> menuSet = roleService.getMenuListWithChecked(roleId);
        String grantMenuJson = JacksonUtil.getObjectMapper().writeValueAsString(menuSet);
        model.addAttribute("type", type);
        model.addAttribute("roleId", roleId);
        model.addAttribute("grantMenuJson", grantMenuJson);
        model.addAttribute("menuSet", menuSet); // Pass as object too for easy rendering
        return "role/grant :: grantFragment";
    }
 
    @PostMapping("/role/grant")
    public ResponseEntity<Void> saveGrant(Long roleId, String menuIds, HttpServletResponse response) {
        roleService.saveGrant(roleId, menuIds);
        response.setHeader("HX-Trigger", "roleListChanged");
        return ResponseEntity.ok().build();
    }

    /**
     * 是否停用角色
     */
    @GetMapping("/role/toggle")
    public ResponseEntity<Void> altRole(@RequestParam long roleId, HttpServletResponse response) {
        roleService.altAvailable(roleId);
        response.setHeader("HX-Trigger", "roleListChanged");
        return ResponseEntity.ok().build();
    }
 
    @DeleteMapping("/role")
    public ResponseEntity<Void> delRole(@RequestParam long roleId) {
        roleService.delRole(roleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/menu.html")
    public String menuList(
            @RequestHeader(value = "HX-Request", required = false) boolean hxRequest,
            Model model) {
        List<Permission> permissionList = roleService.getMenuList();
        model.addAttribute("permissionList", permissionList);
        if (hxRequest) {
            return "menu/menuList :: menuTable";
        }
        return "menu/menuList";
    }
 
    @GetMapping("/menu/edit.html")
    public String menuEdit(@RequestParam(required = false) Long menuId, @RequestParam(required = false) Long parentId, Model model) {
        Permission permission = new Permission();
        permission.setSort(0);
        String parentName = "";
        if (parentId != null) {
            parentName = roleService.getPermissionById(parentId).getName();
            permission.setParentId(parentId);
        }
        if (menuId != null) {
            permission = roleService.getPermissionById(menuId);
            if (!MenuDTO.rootId.equals(permission.getParentId())) {
                parentName = roleService.getPermissionById(permission.getParentId()).getName();
            } else {
                parentName = "顶层菜单";
            }
        }
        model.addAttribute("menu", permission);
        model.addAttribute("parentName", parentName);
        return "menu/menuEdit :: menuEditFragment";
    }
 
    @RequestMapping(value = "/menu", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Void> saveMenu(@Valid Permission permission, HttpServletResponse response) {
        roleService.saveMenu(permission);
        response.setHeader("HX-Trigger", "menuListChanged");
        return ResponseEntity.ok().build();
    }
 
    @DeleteMapping("/menu")
    public ResponseEntity<Void> deleteMenu(@RequestParam long menuId) {
        roleService.delMenu(menuId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logs.html")
    public String loginLogList(
            @PageableDefault(sort = {"loginTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @Valid LoginLog loginLog,
            @RequestHeader(value = "HX-Request", required = false) boolean hxRequest,
            Model model) {
        
        Page<LoginLog> logList = logService.getLoginLogList(
                pageable.withPage(Math.max(0, pageable.getPageNumber() - 1)), loginLog);
        
        model.addAttribute("logPage", logList);
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("logParam", loginLog);
        
        if (hxRequest) {
            return "system/logList :: logTable";
        }
        return "system/logList";
    }

    /**
     * 头像上传
     *
     * @param file 文件
     * @return Result
     */
    @ResponseBody
    @PostMapping("/upload")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.failure(222, "文件为空");
        }
        String path = storageService.uploadFile(file, "avatar");
        return Result.success(path);
    }
}
