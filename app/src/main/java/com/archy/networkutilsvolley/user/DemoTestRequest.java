package com.archy.networkutilsvolley.user;

import com.wangtian.network.request.WTRequest;

/**
 * Created by Apple on 16/4/29.
 */
public class DemoTestRequest extends WTRequest {
    private   String token;
    private   String phonenumber;
    private   int verification;
    private   String passwrod;
    private   String name;

    public DemoTestRequest(String token,String phonenumber,int verification ,String password,String name) {
        super();
        this.token=token;
        this.phonenumber=phonenumber;
        this.verification=verification;
        this.passwrod=password;
        this.name=name;
        this.setMethodName("register");

}
}
