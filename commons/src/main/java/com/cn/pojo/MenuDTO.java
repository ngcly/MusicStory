package com.cn.pojo;

import lombok.Data;

import java.util.List;

/**
 * 树数据封装类
 *
 * @author chen
 * @date 2018-01-02 17:56
 */
@Data
public class MenuDTO {
    public static Long rootId = 0L;

    protected Long id;

    /** 名称 */
    protected String name;

    /** 父id */
    protected Long parentId;

    /** 链接地址 */
    protected String url;

    /** 是否勾选 */
    protected Boolean checked;

    /** 图标 */
    protected String icon;

    /** 子菜单 */
    protected List<MenuDTO> children;

}
