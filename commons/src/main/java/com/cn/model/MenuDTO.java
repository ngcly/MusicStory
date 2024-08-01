package com.cn.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 树数据封装类
 *
 * @author ngcly
 */
@Getter
@Setter
public class MenuDTO implements Serializable {
    public static final Long rootId = 0L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuDTO menuDTO = (MenuDTO) o;
        return id.equals(menuDTO.id) && name.equals(menuDTO.name) && parentId.equals(menuDTO.parentId) && url.equals(menuDTO.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentId, url);
    }
}
