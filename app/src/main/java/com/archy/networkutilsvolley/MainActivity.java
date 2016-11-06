package com.archy.networkutilsvolley;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.archy.networkutilsvolley.user.DemoTestRequest;
import com.archy.networkutilsvolley.user.DemoTestResponse;
import com.archy.networkutilsvolley.user.NetWorkGetTokenRequest;
import com.archy.networkutilsvolley.user.NetWorkGetTokenResponse;
import com.wangtian.network.common.NetWorkManager;
import com.wangtian.network.encrypt.Digest;
import com.wangtian.network.response.WTResponse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private Button aes;

    private String  TAG = "MainActivity";
    private ProgressBar mPb;
    private String mToken;
    private NetWorkManager mNetWorkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
        Application application = getApplication();
        aes = (Button) findViewById(R.id.button1);
        aes.setOnClickListener(this);
        mPb = (ProgressBar) findViewById(R.id.pb);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:

                String imeiString = (this == null || GetMachineInfo.getAndroidId(this) == null) ? "M"
                        : GetMachineInfo.getAndroidId(this);
                String imei = Digest.signMD5(imeiString, "UTF-8").toLowerCase();


                 NetWorkGetTokenRequest request = new NetWorkGetTokenRequest(imei);
                mNetWorkManager =new NetWorkManager(this);
//                mNetWorkManager = new NetWorkManager(this, new NetWorkManager.IDialog() {
//                    @Override
//                    public void show() {
//                        mPb.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void show(String msg) {
//                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void hide(String msg, float delay) {
//
//                    }
//
//
//                    @Override
//                    public void hide() {
//                        mPb.setVisibility(View.GONE);
//                    }
//                });


                mNetWorkManager.request(request, new NetWorkManager.ISuccessResponse() {
                    @Override
                    public void onSuccess(WTResponse response) {

                        NetWorkGetTokenResponse response1 = (NetWorkGetTokenResponse) response;
                        mToken = response1.getToken();
                        Log.d(TAG, mToken);
                    }
                });








              /*  ((MyApplication)getApplication()).getNetWorkManager().request(request, new NetWorkManager.ISuccessResponse() {
                    @Override
                    public void onSuccess(WTResponse response) {
                        Log.d(TAG,response.toString());
                    }
                });*/

               /* ((MyApplication)getApplication()).getNetWorkManager().request(request, new NetWorkManager.ISuccessResponse() {

                    @Override
                    public void onSuccess(WTResponse response) {
                        NetWorkGetTokenResponse resultData = (NetWorkGetTokenResponse) response;
                        String token = resultData.getToken();

                        Log.d(TAG, token);
                        Log.d(TAG, resultData.getCode());
                        Log.d(TAG, resultData.getMsg());

                    }
                }, new NetWorkManager.IErrorResponse() {
                    @Override
                    public void onError(WTErrorResponse response, String errorMessage) {
                        Log.d(TAG, response.getMsg() + "-----" + response.getErrorCode());
                    }
                }, new NetWorkManager.IFinishedResponse() {
                    @Override
                    public void onFinished(WTResponse response, String message) {
                        Log.d(TAG,response.toString()+"----"+message);
                    }
                });*/
                break;
            case R.id.button1:
                mNetWorkManager =new NetWorkManager(this);
             DemoTestRequest request2 = new DemoTestRequest(mToken, "10086", 6762, "123456", "张三");
           //  NetWorkGetVerificationRequest request2 = new NetWorkGetVerificationRequest(mToken, "13467689082");
                mNetWorkManager.request(request2, new NetWorkManager.ISuccessResponse() {
                    @Override
                    public void onSuccess(WTResponse response) {
                        DemoTestResponse response1 = (DemoTestResponse) response;
                       // NetWorkGetVerificationResponse response1 = (NetWorkGetVerificationResponse) response;
                        String response2 = response1.getResponse();
                        if(!TextUtils.isEmpty(response2)){

                            Log.d(TAG,response2);
                        }else {
                            Log.d(TAG, response1.toString());
                        }
                    }
                });
              //  bean.list.getList()
               // data.addAll(list)
                break;
            default:
                break;
        }
    }
}
