package com.archy.networkutilsvolley.user;

import com.wangtian.network.request.WTRequest;

/**
 * Created by Apple on 16/4/25.
 */
public class UserRequest extends WTRequest {
    public UserRequest() {
        super();
        this.setMethodName("/User/Login");
    }
}
