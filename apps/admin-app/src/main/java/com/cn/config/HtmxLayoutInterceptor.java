package com.cn.config;
 
import com.cn.entity.Manager;
import com.cn.entity.Role;
import com.cn.enums.ResourceEnum;
import com.cn.model.MenuDTO;
import com.cn.security.ManagerService;
import com.cn.util.MenuUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
 
import java.util.Collection;
import java.util.List;
 
/**
 * HTMX 与 Thymeleaf 动态模板装配拦截器
 * 1. 浏览器全页请求：自动定位至 layout/main，并注入 contentTemplate 参数完成完整外壳装配
 * 2. HTMX 局部请求：自动定位至 viewName :: mainContent，零外壳飞速局部秒开
 *
 * @author Antigravity
 */
@Component
public class HtmxLayoutInterceptor implements HandlerInterceptor {
    private final ApplicationContext applicationContext;
    private volatile ManagerService managerService;
 
    public HtmxLayoutInterceptor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
 
    private ManagerService getManagerService() {
        if (managerService == null) {
            synchronized (this) {
                if (managerService == null) {
                    managerService = applicationContext.getBean(ManagerService.class);
                }
            }
        }
        return managerService;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && modelAndView.hasView()) {
            String viewName = modelAndView.getViewName();
            if (viewName == null || viewName.startsWith("redirect:") || viewName.startsWith("forward:") || viewName.equals("login") || viewName.startsWith("layout/")) {
                return;
            }
 
            boolean isHtmx = "true".equals(request.getHeader("HX-Request"));
 
            if (isHtmx) {
                // HTMX 异步局部请求：只渲染当前视图的 mainContent 片段
                if (!viewName.contains("::")) {
                    modelAndView.setViewName(viewName + " :: mainContent");
                }
            } else {
                // 浏览器直接回车/刷新（全页访问）：将视图路由为 layout/main 外壳，并把当前视图作为 contentTemplate 动态传入
                modelAndView.addObject("contentTemplate", viewName);
                modelAndView.setViewName("layout/main");
 
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Manager manager) {
                    if (!modelAndView.getModel().containsKey("manager")) {
                        modelAndView.addObject("manager", manager);
                    }
                    if (!modelAndView.getModel().containsKey("menuList")) {
                        Collection<Role> roleList;
                        if (Manager.ADMIN.equals(manager.getUsername())) {
                            roleList = getManagerService().getAdministrator().getRoleList();
                        } else {
                            Manager dbManager = getManagerService().getManagerById(manager.getId());
                            roleList = dbManager.getRoleList();
                        }
                        List<MenuDTO> menuList = roleList.parallelStream()
                                .map(Role::getPermissions)
                                .flatMap(Collection::parallelStream)
                                .filter(permission -> ResourceEnum.MENU == permission.getResourceType())
                                .distinct()
                                .map(MenuDTO.class::cast)
                                .toList();
                        modelAndView.addObject("menuList", MenuUtil.makeMenuToTree(menuList));
                    }
                }
            }
        }
    }
}
