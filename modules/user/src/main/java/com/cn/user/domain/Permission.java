package com.cn.user.domain;
 
import com.cn.enums.ResourceEnum;
import lombok.Getter;
import lombok.Setter;
 
/**
 * 权限领域模型 (Domain Model)
 *
 * @author ngcly
 */
@Getter
@Setter
public class Permission {
    private Long id;
    private String name;
    private String url;
    private String icon;
    private Long parentId;
    private ResourceEnum resourceType;
    private String httpMethod;
    private String parentIds;
    private Integer sort;
}
