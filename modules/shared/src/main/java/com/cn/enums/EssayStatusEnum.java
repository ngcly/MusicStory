package com.cn.enums;

import lombok.Getter;

/**
 * @author ngcly
 */
@Getter
public enum EssayStatusEnum {
    DRAFT("草稿"), PENDING("待审核"), FORBIDDEN("查封"), NORMAL("已确认"), RECOMMEND("推荐");

    private final String msg;

    EssayStatusEnum(String msg) {
        this.msg = msg;
    }

}

