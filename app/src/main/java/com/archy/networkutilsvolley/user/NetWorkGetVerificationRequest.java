package com.archy.networkutilsvolley.user;


import com.wangtian.network.request.WTRequest;

/**
 * Created by weitong on 16/4/7.
 */
public class NetWorkGetVerificationRequest extends WTRequest {


    public NetWorkGetVerificationRequest(String token,String phonenumber) {
        super();
        this.setMethodName("getverification");
        this.setValue("token", token);
        this.setValue("phonenumber",phonenumber);
    }
}
