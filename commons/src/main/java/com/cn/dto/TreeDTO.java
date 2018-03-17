package com.cn.dto;

import lombok.Data;

import java.util.List;

/**
 * 树数据封装类
 *
 * @author chen
 * @date 2018-01-02 17:56
 */
@Data
public class TreeDTO {
    private long id;

    private String name;

    private long parentId;

    private String menuUrl;

    private boolean checked;

    private String icon;

    private List<TreeDTO> children;
    public TreeDTO(long id, String name, long parentId, String url, boolean checked,String icon) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.menuUrl = url;
        this.checked = checked;
        this.icon = icon;
    }
}
