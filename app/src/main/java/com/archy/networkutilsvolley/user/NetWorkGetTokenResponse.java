package com.archy.networkutilsvolley.user;

import com.wangtian.network.response.WTResponse;

/**
 * Created by weitong on 16/4/7.
 */
public class NetWorkGetTokenResponse extends WTResponse {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "NetWorkGetTokenResponse{" +
                "token='" + token + '\'' +
                '}';
    }
}
