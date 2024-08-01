package com.cn.model;

import com.cn.entity.Music;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ngcly
 */
@Data
@Schema(title="创建文章参数", description = "创建文章需要参数")
public class EssayDTO {

    @Schema(title="主键")
    private String id;

    @Schema(title="标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题不可为空")
    private String title;

    @Schema(title="分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类不可为空")
    private Long classifyId;

    @Schema(title="简介", requiredMode = Schema.RequiredMode.REQUIRED)
    private String synopsis;

    @Schema(title="内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容不可为空")
    private String content;

    @Schema(title="音乐列表")
    private List<Music> musicList;

}
