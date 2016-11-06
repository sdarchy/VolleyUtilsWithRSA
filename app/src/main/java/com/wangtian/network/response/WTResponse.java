package com.wangtian.network.response;

/**
 * Created by Archy on 16/4/22.
 */
public class WTResponse {

    /**
     * code : 0
     * msg : 操作成功
     * encrypt_data : uaL0mqFnGKLAi7znrOxikP8B1LebsJgda+g3W2sXjaGF9MJArQ7okZV5latq4aOOhj03wRXt40Vv307M/JUn1Q==
     */

    private String code;
    private String msg;
    private String encrypt_data;

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

    public String getEncrypt_data() {
        return encrypt_data;
    }

    public void setEncrypt_data(String encrypt_data) {
        this.encrypt_data = encrypt_data;

    }

    @Override
    public String toString() {
        return "WTResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", encrypt_data='" + encrypt_data + '\'' +
                '}';
    }
}
