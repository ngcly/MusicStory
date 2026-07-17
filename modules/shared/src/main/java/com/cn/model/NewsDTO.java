package com.cn.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息发送 DTO (防止直接暴露 Entity / 越权传递属性)
 *
 * @author chenning
 */
@Getter
@Setter
public class NewsDTO {

    /** 接收人用户ID (为空时代表广播消息) */
    private Long userId;

    /** 消息内容 */
    @NotBlank(message = "消息内容不可为空")
    private String content;

}
