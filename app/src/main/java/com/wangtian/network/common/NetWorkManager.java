package com.wangtian.network.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wangtian.network.encrypt.AES;
import com.wangtian.network.request.WTRequest;
import com.wangtian.network.response.WTErrorResponse;
import com.wangtian.network.response.WTResponse;
import com.wangtian.network.utils.StringUtils;

/**
 * 基于Volley封装的NetWorkManager
 * 使用前请配置权限
 *  <uses-permission android:name="android.permission.INTERNET"/>
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * Created by Archy on 16/4/22.
 */
public class NetWorkManager {
    private static final String TAG = "NetWorkManager";
    /**
     * 联网交互SuccessCode
     */
    private static final String SUCCESSCODE = "0";
    /**
     * NetWorkErrorCode
     */
    private static final String NOAESKEY = "1000";
    private  IDialog iDialog;

    private Context context;
    private RequestQueue mRequestQueue;


    public NetWorkManager(Context context) {
        this.context = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public NetWorkManager(Context context,IDialog iDialog){
        this.context=context;
        this.iDialog=iDialog;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * 请求成功回调
     */
    public interface ISuccessResponse {
        void onSuccess(WTResponse response);
    }

    /**
     * 请求失败回调
     */
    public interface IErrorResponse {
        void onError(WTErrorResponse response, String errorMessage);
    }
    /**
     * 请求结束回调
     */
    public interface IFinishedResponse{
        void onFinished(WTResponse response,String message);
    }

    /**
     * 超时回调
     */
    public interface ITimeOut{
        void timeOut( float maxTimeOutMs);
    }
    /**
     * 显示加载IDialog
     */
    public interface IDialog{
        void show();
        void show(String msg);
        void hide(String msg,float delay);
        void hide();

    }

    public void request(final WTRequest request, final ISuccessResponse successResponse){
        if(iDialog!=null){
            iDialog.show();
        }

        this.request(request, new ISuccessResponse() {
            @Override
            public void onSuccess(WTResponse response) {
                if (iDialog != null) {
                    iDialog.hide();
                }
                successResponse.onSuccess(response);
            }
        }, new IErrorResponse() {
            @Override
            public void onError(WTErrorResponse response, String errorMessage) {
                if (iDialog != null) {
                    iDialog.show(errorMessage);
                }
                Log.d(TAG, response.toString() + "--errorMessage:" + errorMessage);
                if(response.getMsg()==null){

                    Toast.makeText(context, "网络连接错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new IFinishedResponse() {
            @Override
            public void onFinished(WTResponse response, String message) {
                if (iDialog != null) {
                    iDialog.hide();
                }
            }
        }, new ITimeOut() {

            @Override
            public void timeOut(float maxTimeOutMs) {
                if(iDialog!=null){
                    iDialog.show();
                }
            }
        });
    }





    /**
     * 网络请求
     * 请求方式  请在request对象中设置 this.setMethod（int method）
     * 默认POST请求   WTRequest.POST || WTRequest.GET
     * @param request         请求对象
     * @param successResponse 成功回调
     * @param errorResponse   错误回调
     */
    public void request( final WTRequest request, final ISuccessResponse successResponse,
                         final IErrorResponse errorResponse, final IFinishedResponse finishedResponse,
                            final ITimeOut timeOut) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        WTJsonRequest wtJsonRequest = new WTJsonRequest(request.getMethod(), request, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                WTResponse wtResponse = gson.fromJson(responseStr, WTResponse.class);

                /**
                 * Json中返回码情况
                 code为0表示接口请求成功；
                 code为1表示接口请求失败，可以提示Msg的内容给用户；
                 */

                //code=0
                if (TextUtils.equals(wtResponse.getCode(), SUCCESSCODE)) {
                    String encrypt_data = wtResponse.getEncrypt_data();
                    String aesKey = request.getAesKey();


                    /**
                     * netWork内部错误 没有获取到AESkey
                     */

                    if (TextUtils.isEmpty(aesKey)) {
                        throw new AppRuntimeException("AESkey is not gotten!", NOAESKEY);
                    }
                    String data = AES.decryptFromBase64(encrypt_data, aesKey);
                    if (TextUtils.isEmpty(data)) {
                        throw new AppRuntimeException("There is not responseData!");
                    }
                    try {
                        String requestName = request.getClass().getName();
                        String responseName = StringUtils.substringBeforeLast(requestName, "Request") + "Response";
                        Class responseClass = Class.forName(responseName);
                        Log.d(TAG,data);
                        WTResponse result = (WTResponse) gson.fromJson(data, responseClass);
                        result.setCode(wtResponse.getCode());
                        result.setMsg(wtResponse.getMsg());
                        if (successResponse != null) {
                            successResponse.onSuccess(result);
                            if(finishedResponse!=null){
                                result.setEncrypt_data(data);
                                finishedResponse.onFinished(result,"Success!");
                            }
                        }

                    } catch (Exception e) {
                        if (e instanceof AppRuntimeException) {
                            if (errorResponse != null) {
                                WTErrorResponse wtErrorResponse = new WTErrorResponse();
                                wtErrorResponse.setMsg(wtResponse.getMsg());
                                wtErrorResponse.setErrorCode(wtResponse.getCode());
                                errorResponse.onError(wtErrorResponse, ((AppRuntimeException) e).getMsg());
                                if (finishedResponse!=null){
                                    finishedResponse.onFinished(wtResponse,"Error!");
                                }
                            }
                        } else {
                            e.printStackTrace();
                        }


                    }


                } else {
                    String encrypt_data = wtResponse.getEncrypt_data();
                    String aesKey = request.getAesKey();

                    if (TextUtils.isEmpty(aesKey)) {
                        throw new AppRuntimeException("AESkey is not gotten!", NOAESKEY);
                    }
                    try {
                        String msg = AES.decryptFromBase64(encrypt_data, aesKey);
                        throw new AppRuntimeException(msg, wtResponse.getCode());
                    } catch (Exception e) {
                        if (e instanceof AppRuntimeException) {
                            if (errorResponse != null) {
                                WTErrorResponse wtErrorResponse = new WTErrorResponse();
                                wtErrorResponse.setMsg(wtResponse.getMsg());
                                wtErrorResponse.setErrorCode(wtResponse.getCode());
                                errorResponse.onError(wtErrorResponse, ((AppRuntimeException) e).getMsg());
                                if (finishedResponse!=null){
                                    finishedResponse.onFinished(wtResponse,"Error!");
                                }
                            }
                        } else {
                            e.printStackTrace();
                        }


                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onError -> " + error.getMessage());
                WTErrorResponse wtErrorResponse =new WTErrorResponse();
                wtErrorResponse.setMsg(error.getMessage());
                wtErrorResponse.setErrorCode("1");
                if (errorResponse != null) {
                    errorResponse.onError(wtErrorResponse, "发生错误");
                    if (finishedResponse!=null){
                        WTResponse wtResponse = new WTResponse();
                        finishedResponse.onFinished(wtResponse,"Error!");
                    }
                }

            }
        });

        wtJsonRequest.setRetryPolicy(

        new WTRetryPolicy(new WTRetryPolicy.NetworkAccessTimeOut() {

            @Override
            public void timeOut(float maxTimeOutMs) {
                timeOut.timeOut(maxTimeOutMs);
            }
        }));
       // wtJsonRequest.getRetryPolicy();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null){
            mRequestQueue.add(wtJsonRequest);
        }else {

            WTErrorResponse wtErrorResponse =new WTErrorResponse();
            wtErrorResponse.setMsg("请检查网络连接是否打开");
            wtErrorResponse.setErrorCode("1");
            if (errorResponse != null) {
                errorResponse.onError(wtErrorResponse, "发生错误");
                if (finishedResponse!=null){
                    WTResponse wtResponse = new WTResponse();
                    finishedResponse.onFinished(wtResponse,"Error!");
                }
            }
        }

    }


}
