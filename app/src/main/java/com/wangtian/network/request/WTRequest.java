package com.wangtian.network.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wangtian.network.base.Const;
import com.wangtian.network.utils.AESKeyUtils;
import com.wangtian.network.encrypt.AES;
import com.wangtian.network.encrypt.RSA;

import java.util.HashMap;

/**
 *
 * Created by Archy on 16/4/22.
 */
public class WTRequest {
    //请求地址
    public final static String kAPI = Const.BASE_URL;
    /**请求方式*/
    private int mMethod;
    /**
     * 网络请求方式
     */
    public static final int POST = Request.Method.POST;
    public static final int GET = Request.Method.GET;

    private String methodName;
    private HashMap<String,Object> paramMap;
    private String aesKey;
   // private  String publicKey=Const.PUBLIC_KEY;

    public WTRequest() {
       this.mMethod= POST;
        this.paramMap=new HashMap<>();
    }

    /**
     * 转换请求连接
     * @return 请求连接
     */
    public String stringOfAPI(){
        if (Request.Method.GET==this.mMethod){
            return kAPI+"?"+"";
        }else{
            return kAPI+"?cmd="+methodName;
        }
    }

    /**
     * String转化为请求体
     * @return
     */
    public String stringOfPostBody(){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String jsonStr = gson.toJson(paramMap);

        //AES加密
        aesKey = AESKeyUtils.getKey();
        byte[] data = jsonStr.getBytes();
        String  encrypt_data = AES.encryptToBase64(jsonStr, aesKey);
        //RSA加密
        try {

            String encrypt_key = RSA.encrypt(aesKey, Const.PUBLIC_KEY);
            HashMap<String, String> strHashMap = new HashMap<>();
            strHashMap.put("encrypt_data", encrypt_data);
            strHashMap.put("encrypt_key",encrypt_key);
            String json = gson.toJson(strHashMap);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("WTRequest","Something is wrong with RSA");
        }

        return null;
    }



    /**
     * 设置请求体数据
     * @param key 键
     * @param val 值
     */

    public void setValue(String key,String val){
        if (TextUtils.isEmpty(key)|| val==null){
            return;
        }
        this.paramMap.put(key,val);
    }



    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }



    public HashMap<String, Object> getParamMap() {
        return paramMap;
    }

    public int getMethod() {
        return mMethod;
    }

    public void setMethod(int method) {
        mMethod = method;
    }

    public String getAesKey() {
        return aesKey;
    }
}
