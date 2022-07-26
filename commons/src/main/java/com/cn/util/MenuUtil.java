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
    public static <T extends MenuDTO> Set<MenuDTO> checkMenuSelected(List<T> originMenus, List<T> roleMenus) {
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
    public static Set<MenuDTO> makeMenuToTree(List<MenuDTO> menuList) {
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
        return rootTrees;
    }

    /**
     * 将菜单进行树状排序
     */
    public static <T extends MenuDTO> List<T> menuTreeSort(List<T> list) {
        List<T> resultList = new ArrayList<>();
        sortList(list, MenuDTO.rootId, resultList);
        return resultList;
    }

    public static <T extends MenuDTO> void sortList(List<T> list, long id,List<T> resultList) {
        for (int i = 0, j = list.size(); i < j; i++) {
            T menu = list.get(i);
            if (menu.getParentId() == id) {
                resultList.add(menu);
                sortList(list, menu.getId(),resultList);
            }
        }
    }
}
