package com.wangtian.network.common;

/**
 * Created by weitong on 16/4/7.
 */
public class AppRuntimeException extends  RuntimeException {


    private String msg;
    private String code;

    public AppRuntimeException() {
        this.code = "1";
        this.msg = "发生错误";
    }

    public AppRuntimeException(String msg) {
        this.code = "1";
        this.msg = msg;
    }

    public AppRuntimeException(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
