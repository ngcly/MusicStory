package com.cn;

import com.cn.enums.GenderEnum;
import com.cn.enums.UserStatusEnum;
import com.cn.enums.UserTypeEnum;
import com.cn.exception.GlobalException;
import com.cn.dao.ManagerRepository;
import com.cn.entity.Manager;
import com.cn.entity.Role;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台客服管理员 service类
 *
 * @author ngcly
 * @since 2018-01-02 17:50
 */
@Service
@AllArgsConstructor
public class ManagerService implements UserDetailsService {
    private final ManagerRepository managerRepository;
    private final RoleService roleService;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException 用户名未找到
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (Manager.ADMIN.equals(username)) {
            return getAdministrator();
        } else {
            return managerRepository.findManagerByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                    "用户不存在"));
        }
    }

    /**
     * 根据Id获取管理员信息
     *
     * @param managerId 管理员id
     * @return Manager
     */
    public Manager getManagerById(Long managerId) {
        if (managerId == 0) {
            return getAdministrator();
        }
        return managerRepository.findManagerById(managerId).orElseThrow();
    }

    /**
     * 获取管理员列表
     *
     * @param pageable 分页
     * @return Page<Manager>
     */
    public Page<Manager> getManagersList(Pageable pageable, Manager manager) {
        return managerRepository.findAll(ManagerRepository.getManagerList(manager.getUsername(),
                manager.getState(), manager.getGender(), manager.getBeginTime(), manager.getEndTime()), pageable);
    }

    /**
     * 新增或更新管理员信息
     *
     * @param curManager    当前操作人
     * @param updateManager 被修改人
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveManager(Manager curManager, Manager updateManager) {
        Set<Role> roleList = curManager.getRoleList();
        //获取被修改人之前的角色-当前人的角色=必存角色 最后结果为必存角色+回传角色
        Set<Role> allRole = new HashSet<>(curManager.getRoleList());

        Set<Role> roles = new HashSet<>();
        if (Objects.nonNull(updateManager.getId())) {
            Manager manager = managerRepository.getReferenceById(updateManager.getId());
            //判断是否当前人在改自己的信息
            if (!curManager.getId().equals(updateManager.getId())) {
                updateManager.setUsername(manager.getUsername());
            }
            updateManager.setPassword(manager.getPassword());
            allRole.addAll(manager.getRoleList());
            manager.getRoleList().removeAll(roleList);
            //加上必存角色 防止越权篡改数据
            roles.addAll(manager.getRoleList());
        } else {
            updateManager.setPassword(passwordEncoder.encode("123456"));
            updateManager.setState(UserStatusEnum.INITIALIZE);
        }
        if (!StringUtils.hasText(updateManager.getUsername())) {
            throw new GlobalException(333, "用户名不可为空");
        }
        if (managerRepository.existsByUsernameAndIdIsNot(updateManager.getUsername(), updateManager.getId())) {
            throw new GlobalException(333, "该用户名已存在");
        }

        if (updateManager.getRoleIds() != null) {
            List<Long> roleIds = Arrays.asList(updateManager.getRoleIds());
            allRole.stream().filter(role -> roleIds.contains(role.getId())).forEach(roles::add);
            updateManager.setRoleList(roles);
        }
        managerRepository.save(updateManager);
    }

    /**
     * 更新管理员信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateManager(Manager manager) {
        managerRepository.save(manager);
    }

    /**
     * 删除管理员
     */
    @Transactional(rollbackFor = Exception.class)
    public void delManager(Long managerId) {
        managerRepository.deleteById(managerId);
    }

    /**
     * 修改管理员密码
     *
     * @param managerId 管理员id
     * @param password  密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long managerId, String oldPassword, String password) {
        Manager manager = managerRepository.getReferenceById(managerId);
        if (!passwordEncoder.matches(oldPassword, manager.getPassword())) {
            throw new GlobalException(333, "原密码错误");
        }
        manager.setPassword(passwordEncoder.encode(password));
    }

    /**
     * 重置密码
     *
     * @param managerId 管理员id
     * @param password  修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long managerId, String password) {
        Manager manager = managerRepository.getReferenceById(managerId);
        manager.setPassword(passwordEncoder.encode(password));
    }


    /**
     * 获取url权限元数据
     *
     * @return List<String>
     */
    public List<String> getUrlPermissionMetadata() {
        return roleService.getUrlPermission();
    }

    public Manager getAdministrator() {
        List<Role> roles = roleService.getAvailableRoles(UserTypeEnum.ADMIN);
        Manager manager = new Manager();
        manager.setId(0L);
        manager.setUsername(Manager.ADMIN);
        manager.setPassword(passwordEncoder.encode("123456"));
        manager.setGender(GenderEnum.MAN);
        manager.setRealName("超管员");
        manager.setAvatar("https://music-story.oss-cn-hongkong.aliyuncs.com/uPic/beautify.png");
        manager.setBirthday(LocalDate.of(1993, 7, 24));
        manager.setState(UserStatusEnum.NORMAL);
        manager.setRoleList(new HashSet<>(roles));
        roles.forEach(role -> Hibernate.initialize(role.getPermissions()));
        return manager;
    }

    private List<String> getAllUrlPermission() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

        // 创建一个Set来存储格式化后的映射（HTTP方法_路径）
        Set<String> formattedMappings = new LinkedHashSet<>();

        handlerMethods.forEach((mappingInfo, handlerMethod) -> {
            // 获取HTTP方法
            Set<String> httpMethods = new HashSet<>();
            mappingInfo.getMethodsCondition().getMethods().forEach(method ->
                    httpMethods.add(method.name()));

            // 如果没有指定HTTP方法，默认添加所有HTTP方法
            if (httpMethods.isEmpty()) {
                httpMethods.addAll(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).collect(Collectors.toSet()));
            }

            // 获取URL路径
            Set<String> patterns = new HashSet<>();
            if (mappingInfo.getPathPatternsCondition() != null) {
                mappingInfo.getPathPatternsCondition().getPatterns().forEach(pattern ->
                        patterns.add(pattern.getPatternString()));
            } else if (mappingInfo.getPatternsCondition() != null) {
                patterns.addAll(mappingInfo.getPatternsCondition().getPatterns());
            }

            // 为每个HTTP方法和每个URL路径的组合创建一个格式化的字符串
            for (String httpMethod : httpMethods) {
                for (String pattern : patterns) {
                    formattedMappings.add(httpMethod + "_" + pattern);
                }
            }
        });
        return new ArrayList<>(formattedMappings);
    }
}
