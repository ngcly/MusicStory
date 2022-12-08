package com.cn.enums;

/**
 * @author ngcly
 */
public enum EssayState {
    DRAFT("草稿"), PENDING("待审核"), FORBIDDEN("查封"), NORMAL("已确认"), RECOMMEND("推荐");

    private final String msg;

    EssayState(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}

