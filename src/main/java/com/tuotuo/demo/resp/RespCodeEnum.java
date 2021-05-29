package com.tuotuo.demo.resp;

/**
 * @Author xuconghui
 * @Date 2020/11/18 15:14
 * @Version 1.0
 **/
public enum RespCodeEnum {
    SUCC("200", "success"),
    FAIL("001", "网络异常");
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    RespCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
