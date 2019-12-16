package com.cn.enums;

/**
 * @author ngcly
 */
public class CommonState {

    public enum EssayState {
        DRAFT((byte)0, "草稿"), PENDING((byte)1, "待审核"), FORBIDDEN((byte)2, "查封"), NORMAL((byte)3, "已确认"), RECOMMEND((byte)4, "推荐");

        private byte code;
        private String msg;

        EssayState(byte code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public byte getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
