package com.cn.controller;

import com.cn.LogService;
import com.cn.ManagerService;
import com.cn.RoleService;
import com.cn.entity.*;
import com.cn.pojo.MenuDTO;
import com.cn.util.Result;
import com.cn.util.UploadUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

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
    private final ObjectMapper objectMapper;

    /**
     * 权限注解说明
     * 下面两个权限注解底层实现一样  唯一区别就是hasRole默认添加 ROLE_ 前缀
     * @PreAuthorize("hasAuthority('ROLE_admin')")
     * @PreAuthorize("hasRole('admin')") 方法调用前判断是否有权限
     * @PreAuthorize("hasPermission('sys','created')") 自定义判断权限标识符
     * @PostAuthorize("returnObject.id%2==0") 方法调用完后判断 若为false则无权限  基本用不上
     * @PostFilter("filterObject.id%2==0") 对返回结果进行过滤  filterObject内置为返回对象
     * @PreFilter(filterTarget="ids", value="filterObject%2==0") 对方法参数进行过滤 如有多参则指定参数 ids为其中一个参数
     * 上述注解方式属于静态鉴权 由于本项目采用了动态鉴权模式:@link MyAuthorizationManager .所以上述注解并未使用到
     *
     * 管理员列表页
     */
    @GetMapping("/manager.html")
    public String managerList() {
        return "manager/managerList";
    }

    @ResponseBody
    @GetMapping("/manager")
    public Result<List<Manager>> getManagerList(@PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC)Pageable pageable,
                                                @Valid Manager manager) {
        Page<Manager> managerList = managerService.getManagersList(
                pageable.withPage(pageable.getPageNumber()-1), manager);
        return Result.success(managerList.getTotalElements(), managerList.getContent());
    }

    /**
     * 管理员详情页
     */
    @GetMapping("/manager/{managerId}")
    public String managerDetail(@PathVariable Long managerId, Model model) {
        Manager manager = managerService.getManagerById(managerId);
        model.addAttribute("manager", manager);
        return "manager/managerDetail";
    }

    /**
     * 管理员编辑页
     */
    @GetMapping("/manager/edit.html")
    public String altManager(@AuthenticationPrincipal Manager managerDetail, @RequestParam(required = false) Long managerId, Model model) {
        //最好是从当前授权信息里面提出角色列表来
        Set<Role> roles = managerDetail.getRoleList();
        if (Manager.ADMIN.equals(managerDetail.getUsername())) {
            roles = roleService.getAvailableRoles(Role.ROLE_TYPE_MANAGER);
        }
        String optRole = roles.stream().map(role -> role.getId().toString()).collect(Collectors.joining(","));
        Manager manager = new Manager();
        String checkRoleIds = "";
        if (managerId != null) {
            manager = managerService.getManagerById(managerId);
            checkRoleIds = manager.getRoleList().stream().map(role ->
                    role.getId().toString()).collect(Collectors.joining(","));
            roles.addAll(manager.getRoleList());
        }
        model.addAttribute("currentId", managerDetail.getId());
        model.addAttribute("manager", manager);
        //待选角色列表
        model.addAttribute("roles", roles);
        //已勾选角色ID
        model.addAttribute("checkRoleId", checkRoleIds);
        //可授权角色ID
        model.addAttribute("optionRoles", optRole);
        return "manager/managerEdit";
    }

    /**
     * 新增或修改管理员信息
     */
    @ResponseBody
    @PostMapping("/manager")
    public Result<?> addManager(@Valid Manager manager) {
        Manager managerDetail = (Manager) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        managerService.saveManager(managerDetail, manager);
        return Result.success();
    }

    @ResponseBody
    @PutMapping("/manager")
    public Result<?> updateManager(@Valid Manager manager) {
        Manager managerDetail = (Manager) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        managerService.saveManager(managerDetail, manager);
        return Result.success();
    }

    /**
     * 删除管理员
     */
    @ResponseBody
    @DeleteMapping("/manager")
    public Result<?> deleteManager(@RequestParam Long managerId) {
        Manager managerDetail = (Manager) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (managerDetail.getId().equals(managerId) || Long.valueOf(1).equals(managerId)) {
            return Result.failure(333, "禁止删除自己和admin");
        }
        managerService.delManager(managerId);
        return Result.success();
    }

    /**
     * 修改密码页面
     *
     * @return String
     */
    @GetMapping("/manager/pwd.html")
    public String altPwd() {
        return "manager/updatePwd";
    }

    /**
     * 修改密码
     */
    @ResponseBody
    @PutMapping("/manager/pwd")
    public Result<?> updatePassword(@AuthenticationPrincipal Manager manager, @RequestParam String oldPassword,
                                    @RequestParam String password) {
        managerService.updatePassword(manager.getId(), oldPassword, password);
        return Result.success();
    }

    /**
     * 重置管理员密码
     *
     * @param managerId 管理员id
     */
    @ResponseBody
    @PostMapping("/manager/pwd")
    public Result<?> resetPassword(@RequestParam Long managerId) {
        managerService.updatePassword(managerId, "123456");
        return Result.success();
    }

    /**
     * 角色列表页
     */
    @GetMapping("/role.html")
    public String roleList() {
        return "role/roleList";
    }

    /**
     * 角色数据
     * @param pageable 分页
     * @param role 条件数据
     * @return List<Role>
     */
    @GetMapping("/role")
    @ResponseBody
    public Result<List<Role>> roleList(@PageableDefault(sort = {"roleName"}, direction = Sort.Direction.DESC)Pageable pageable,
                                       @Valid Role role) {
        Page<Role> roleList = roleService.getRoleList(
                pageable.withPage(pageable.getPageNumber()-1), role);

        return Result.success(roleList.getTotalElements(), roleList.getContent());
    }

    /**
     * 新增或修改角色页面
     */
    @RequestMapping("/role/edit.html")
    public String roleEdit(@RequestParam(required = false) Long roleId, Model model) {
        Role role = new Role();
        if (roleId != null) {
            role = roleService.findRole(roleId);
        }
        model.addAttribute("role", role);
        return "role/roleEdit";
    }

    /**
     * 新增角色
     */
    @ResponseBody
    @PostMapping("/role")
    public Result<?> addRole(@Valid Role role) {
        roleService.saveRole(role);
        return Result.success();
    }

    /**
     * 修改角色
     * @param role 修改内容
     */
    @ResponseBody
    @PutMapping("/role")
    public Result<?> updateRole(@Valid Role role) {
        roleService.saveRole(role);
        return Result.success();
    }

    /**
     * 角色授权页面
     */
    @GetMapping("/role/grant.html")
    public String grantForm(@RequestParam long roleId, @RequestParam String type, Model model) throws JsonProcessingException {
        Set<MenuDTO> menuSet = roleService.getMenuListWithChecked(roleId);
        String menuList = objectMapper.writeValueAsString(menuSet);
        model.addAttribute("type", type);
        model.addAttribute("roleId", roleId);
        model.addAttribute("menuList", menuList);
        return "role/grant";
    }

    /**
     * 保存授权
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     * @return Result
     */
    @PostMapping("/role/grant")
    @ResponseBody
    public Result<?> saveGrant(Long roleId, String menuIds) {
        roleService.saveGrant(roleId, menuIds);
        return Result.success();
    }

    /**
     * 是否停用角色
     */
    @GetMapping("/role/toggle")
    @ResponseBody
    public Result<?> altRole(@RequestParam long roleId) {
        roleService.altAvailable(roleId);
        return Result.success();
    }

    /**
     * 删除角色
     */
    @ResponseBody
    @DeleteMapping("/role")
    public Result<?> delRole(@RequestParam long roleId) {
        roleService.delRole(roleId);
        return Result.success();
    }

    /**
     * 菜单列表页
     */
    @GetMapping("/menu.html")
    public String menuList(Model model) {
        List<Permission> menuList = roleService.getMenuList();
        model.addAttribute("menuList", menuList);
        return "menu/menuList";
    }

    /**
     * 新增或修改菜单页
     */
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
        return "menu/menuEdit";
    }

    /**
     * 新增菜单
     */
    @ResponseBody
    @PostMapping("/menu")
    public Result<?> addMenu(@Valid Permission permission) {
        roleService.saveMenu(permission);
        return Result.success();
    }

    /**
     * 修改菜单
     */
    @ResponseBody
    @PutMapping("/menu")
    public Result<?> updateMenu(@Valid Permission permission) {
        roleService.saveMenu(permission);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @ResponseBody
    @DeleteMapping("/menu")
    public Result<?> deleteMenu(@RequestParam long menuId) {
        roleService.delMenu(menuId);
        return Result.success();
    }

    /**
     * 登录日志列表页
     */
    @GetMapping("/logs.html")
    public String loginLogList() {
        return "system/logList";
    }

    @ResponseBody
    @GetMapping("/logs")
    public Result<List<LoginLog>> getLogList(@PageableDefault(sort = {"loginTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                                             @Valid LoginLog loginLog) {

        Page<LoginLog> logList = logService.getLoginLogList(
                pageable.withPage(pageable.getPageNumber()-1), loginLog);
        return Result.success(logList.getTotalElements(), logList.getContent());
    }

    /**
     * 头像上传
     *
     * @param file 文件
     * @return Result
     */
    @ResponseBody
    @RequestMapping("/upload")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.failure(222, "文件为空");
        }
        String path = UploadUtil.uploadFileByAli(file, "avatar");
        return Result.success(path);
    }
}
