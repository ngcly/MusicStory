package com.cn.util;

import cn.hutool.core.collection.CollectionUtil;
import com.cn.pojo.MenuDTO;

import java.util.*;

/**
 * 菜单工具类
 *
 * @author ngcly
 * @date 2018-01-02 17:54
 */
public final class MenuUtil {
    private MenuUtil() {
    }

    /**
     * 判断菜单是否被勾选
     *
     * @param originMenus 原菜单
     * @param roleMenus   角色对应菜单
     */
    public static <T extends MenuDTO> Collection<MenuDTO> checkMenuSelected(Collection<T> originMenus, Collection<T> roleMenus) {
        List<MenuDTO> menuList = new ArrayList<>();
        originMenus.forEach(t -> {
            t.setChecked(roleMenus.contains(t));
            menuList.add(t);
        });
        return makeMenuToTree(menuList);
    }

    /**
     * 将菜单list转换成树状
     */
    public static Collection<MenuDTO> makeMenuToTree(Collection<MenuDTO> menuList) {
        Set<MenuDTO> rootTrees = new HashSet<>();
        menuList.forEach(menu -> {
            if(MenuDTO.rootId.equals(menu.getParentId())){
                rootTrees.add(menu);
            }
            menuList.forEach(menuDTO -> {
                if(menu.getId().equals(menuDTO.getParentId())){
                    if(menu.getChildren()==null){
                        menu.setChildren(CollectionUtil.newArrayList(menuDTO));
                    }else{
                        menu.getChildren().add(menuDTO);
                    }
                }
            });
        });
        return menuTreeSort(rootTrees);
    }

    /**
     * 将菜单进行树状排序
     */
    public static <T extends MenuDTO> List<T> menuTreeSort(Collection<T> list) {
        List<T> resultList = new ArrayList<>();
        sortList(list, MenuDTO.rootId, resultList);
        return resultList;
    }

    public static <T extends MenuDTO> void sortList(Collection<T> list, Long id, Collection<T> resultList) {
        list.forEach(menu -> {
            if(id.equals(menu.getParentId())) {
                resultList.add(menu);
                sortList(list, menu.getId(),resultList);
            }
        });
    }
}
