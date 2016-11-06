package com.wangtian.network.common;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wangtian.network.request.WTRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by Archy on 16/4/25.
 */
public class WTJsonRequest extends Request<String> {

    /**
     * 编码格式
     * */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /**生成content-type*/
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Response.Listener<String> mListener;
    private final String mRequestBody;
    private final WTRequest request;
    private String TAG="WTJsonRequest";

    public WTJsonRequest(int method, WTRequest request, Response.Listener<String> listener,
                       Response.ErrorListener errorListener) {
        super(method, request.stringOfAPI(), errorListener);
        this.request =request;
        mListener = listener;
        mRequestBody = request.stringOfPostBody();

    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

            HashMap<String, String> errorMap = null;

            if (response.statusCode == 200 || response.statusCode == 304) {

                String responseStr = new String(response.data);
                return Response.success(responseStr,HttpHeaderParser.parseCacheHeaders(response));

            }
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            errorMap = new HashMap<>();
            errorMap.put("errorCode", String.valueOf(response.statusCode));
            errorMap.put("msg", new String(response.data));
        Log.d(TAG,gson.toJson(errorMap));
            return Response.error(new VolleyError(gson.toJson(errorMap)));



    }

    @Override
    protected void deliverResponse(String response) {
         mListener.onResponse(response);
    }
    @Override
    public byte[] getPostBody() {
        return getBody();
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }



}
