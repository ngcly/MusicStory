package com.cn.dto;

import java.util.List;

/**
 * 树数据封装类
 *
 * @author chen
 * @date 2018-01-02 17:56
 */
public class TreeDTO {
    private long id;

    private String name;

    private long parentId;

    private String menuUrl;

    private boolean checked;

    private List<TreeDTO> children;
    public TreeDTO(long id, String name, long parentId, String url, boolean checked) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.menuUrl = url;
        this.checked = checked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public List<TreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeDTO> children) {
        this.children = children;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Tree [id=" + id + ", name=" + name + ", parentId=" + parentId
                + ", childrens=" + children + "]";
    }
}
