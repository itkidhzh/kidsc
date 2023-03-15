package com.kidsc.commons;

/**
 * @ClassName
 * @Description: TODO 请求状态响应码
 * @Author: kidhzh@outlook.com
 */
public enum  Code {
        OK("10000","请求成功"),
        ERROR("10001","请求失败"),
        NO_LOGIN("10002","没有登录"),
        NO_GOODS_STORE("10003","没有库存"),
        NO_CONFIRM_ORDERS("11000","没有确认订单");

        private String code;
        private String msg;
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        Code(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
}
