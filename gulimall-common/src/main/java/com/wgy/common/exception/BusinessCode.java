package com.wgy.common.exception;

/**
 * @Author WGY
 * @Date 2021/3/7
 * @Time 20:28
 * To change this template use File | Settings | File Templates.
 **/
public enum BusinessCode {
    /**
     * 系统未知异常
     */
    UN_KNOW_ERROR(10000, "系统未知异常"),
    /**
     * 参数格式校验失败
     */
    PARAM_VALID_ERROR(10001, "参数格式校验失败")
    ;
    private Integer code;
    private String msg;

    BusinessCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
