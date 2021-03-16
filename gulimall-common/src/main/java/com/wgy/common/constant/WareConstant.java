package com.wgy.common.constant;

/**
 * @Author WGY
 * @Date 2021/3/14
 * @Time 19:46
 * To change this template use File | Settings | File Templates.
 **/
public class WareConstant {
    public enum PurchaseStatusEum{
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        RECEIVED(2, "已领取"),
        FINISH(3, "已完成"),
        ERROR(4, "有异常");
        private int code;
        private String msg;
        PurchaseStatusEum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }
    public enum PurchaseDetailStatusEum{
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"),
        FINISH(3, "已完成"),
        ERROR(4, "采购失败");
        private int code;
        private String msg;
        PurchaseDetailStatusEum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }
}
