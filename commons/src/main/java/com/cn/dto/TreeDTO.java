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
    private Long id;

    private String name;

    private Long parentId;

    private String menuUrl;

    private Boolean checked;

    private String ico;

    private List<TreeDTO> children;
    public TreeDTO(long id, String name, long parentId, String url, boolean checked,String icon) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.menuUrl = url;
        this.checked = checked;
        this.ico = icon;
    }
}
