package com.cn.util;

import com.cn.dto.TreeDTO;
import com.cn.entity.Permission;

import java.util.*;

/**
 * 菜单工具类
 *
 * @author chen
 * @date 2018-01-02 17:54
 */
public class MenuUtil {
    static List<Permission> resultList;

    /**
     * 根据条件将菜单转成Tree对象
     */
    public static Set<TreeDTO> makeTreeList(List<Permission> originMenus){
        Set<TreeDTO> trees = new LinkedHashSet<>();
        TreeDTO tree1;
        for(Permission permission:originMenus){
            if("menu".equals(permission.getResourceType())){
                tree1 = new TreeDTO(permission.getId(),permission.getName(),permission.getParentId(),permission.getUrl(),false);
                trees.add(tree1);
            }
        }
        return eachTree(trees);
    }

    public static Set<TreeDTO> makeTreeList(List<Permission> originMenus,List<Permission> roleMenus){
        Set<TreeDTO> trees = new HashSet<>();
        TreeDTO tree1;
        boolean contained;
        for(Permission sysPermission:originMenus){
            contained=false;
            for(Permission rolePermission:roleMenus){
                if(sysPermission.getId()==rolePermission.getId()){
                    contained=true;
                    break;
                }
            }
            if(contained){
                tree1 = new TreeDTO(sysPermission.getId(),sysPermission.getName(),sysPermission.getParentId(),sysPermission.getUrl(),true);
            }else {
                tree1 = new TreeDTO(sysPermission.getId(),sysPermission.getName(),sysPermission.getParentId(),sysPermission.getUrl(),false);
            }
            trees.add(tree1);
        }
        return eachTree(trees);
    }

    /**
     * 将已转成Tree对象的list进行转换成树状
     */
    public static Set<TreeDTO> eachTree(Set<TreeDTO> trees){
        Set<TreeDTO> rootTrees = new HashSet<>();
        for (TreeDTO tree : trees) {
            if(tree.getParentId() == 0){
                rootTrees.add(tree);
            }
            for (TreeDTO t : trees) {
                if(t.getParentId() == tree.getId()){
                    if(tree.getChildren() == null){
                        List<TreeDTO> myChildrens = new ArrayList<>();
                        myChildrens.add(t);
                        tree.setChildren(myChildrens);
                    }else{
                        tree.getChildren().add(t);
                    }
                }
            }
        }
        return rootTrees;
    }

    /**
     * 将菜单根据树状排序
     */
    public static List<Permission> treeOrderList(List<Permission> sysPermissions){
        resultList = new ArrayList<>();
        sortList(sysPermissions,0);
        return resultList;
    }

    public static void sortList(List<Permission> list,long id){
        for(Permission permission:list){
            if(permission.getParentId() == id){
                resultList.add(permission);
                sortList(list,permission.getId());
            }
        }
    }
}
