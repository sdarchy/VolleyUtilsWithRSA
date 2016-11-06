package com.wangtian.network.response;

/**
 * Created by Apple on 16/4/26.
 */
public class WTErrorResponse {

    /**
     * msg : {  }
     * errorCode : 0
     */

    private String msg;
    private String errorCode;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "WTErrorResponse{" +
                "msg='" + msg + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}
