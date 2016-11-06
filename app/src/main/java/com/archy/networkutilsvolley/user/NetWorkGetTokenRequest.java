package com.archy.networkutilsvolley.user;


import com.wangtian.network.request.WTRequest;

/**
 * Created by weitong on 16/4/7.
 */
public class  NetWorkGetTokenRequest extends WTRequest {

    private final String appid;

    public NetWorkGetTokenRequest(String appid) {
        super();
        this.appid=appid;
        this.setMethodName("gettoken");
    }

}
