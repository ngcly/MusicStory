package com.cn.pojo;

import com.cn.entity.Music;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ngcly
 */
@ApiModel(value="创建文章参数", description = "创建文章需要参数")
@Getter
@Setter
public class EssayDTO {

    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="标题", required = true)
    @NotBlank(message = "标题不可为空")
    private String title;

    @ApiModelProperty(value="分类", required = true)
    @NotNull(message = "分类不可为空")
    private Long classifyId;

    @ApiModelProperty(value="简介", required = true)
    private String synopsis;

    @ApiModelProperty(value="内容", required = true)
    @NotBlank(message = "内容不可为空")
    private String content;

    @ApiModelProperty(value="音乐列表")
    private List<Music> musicList;

}
