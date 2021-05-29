package com.tuotuo.demo.resp;



import java.io.Serializable;

/**
 * @Author xuconghui
 * @Date 2020/10/23 13:14
 * @Version 1.0
 **/

public class BaseResp<T> implements Serializable {
    private static final long serialVersionUID = -5428441606279914878L;
    private String code;
    private String message;
    private T data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private BaseResp<T> setCode(String code) {
        this.code = code;
        return this;
    }

    private BaseResp<T> setMsg(String msg) {
        this.message = msg;
        return this;
    }

    private BaseResp<T> setNewData(T data) {
        this.data = data;
        return this;
    }


    public static <V> BaseResp<V> success(String code, String msg, V data) {
        return new BaseResp<V>().setCode(code).setMsg(msg).setNewData(data);
    }
    public static <V> BaseResp<V> judge(String msg) {
        if(RespCodeEnum.SUCC.getMessage().equals(msg)){
            return new BaseResp<V>().setCode(RespCodeEnum.SUCC.getCode()).setMsg(msg);
        }else{
            return new BaseResp<V>().setCode(RespCodeEnum.FAIL.getCode()).setMsg(msg);
        }
    }

    public static <V> BaseResp<V> success(V data) {
        return new BaseResp<V>().setCode(RespCodeEnum.SUCC.getCode()).setMsg(RespCodeEnum.SUCC.getMessage()).setNewData(data);
    }

    public static <V> BaseResp<V> success(String msg, V data) {
        return new BaseResp<V>().setCode(RespCodeEnum.SUCC.getCode()).setMsg(msg).setNewData(data);
    }

    public static <V> BaseResp<V> success(String code, String msg) {
        return new BaseResp<V>().setCode(code).setMsg(msg);
    }

    public static <V> BaseResp<V> error(String code, String msg, V data) {
        return new BaseResp<V>().setCode(code).setMsg(msg).setNewData(data);
    }

    public static <V> BaseResp<V> error(String msg, V data) {
        return new BaseResp<V>().setCode(RespCodeEnum.FAIL.getCode()).setMsg(msg).setNewData(data);
    }

    public static <V> BaseResp<V> error(V data) {
        return new BaseResp<V>().setCode(RespCodeEnum.FAIL.getCode()).setMsg(RespCodeEnum.FAIL.getMessage()).setNewData(data);
    }

    public static <V> BaseResp<V> error() {
        return new BaseResp<V>().setCode(RespCodeEnum.FAIL.getCode()).setMsg(RespCodeEnum.FAIL.getMessage());
    }

    public static <V> BaseResp<V> error(String msg) {
        return new BaseResp<V>().setCode(RespCodeEnum.FAIL.getCode()).setMsg(msg);
    }

    public static <V> BaseResp<V> error(String code, String msg) {
        return new BaseResp<V>().setCode(code).setMsg(msg);
    }

}
