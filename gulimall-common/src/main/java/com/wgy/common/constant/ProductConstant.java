package com.wgy.common.constant;

/**
 * @Author WGY
 * @Date 2021/3/10
 * @Time 21:08
 * To change this template use File | Settings | File Templates.
 **/
public class ProductConstant {

    public enum AttrEum{
        ATTR_TYPE_BASE(1, "基本属性"),
        ATTR_TYPE_SALE(0, "销售属性");
        private int code;
        private String msg;
        AttrEum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }
}
