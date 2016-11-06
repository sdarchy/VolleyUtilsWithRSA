package com.wangtian.network.common;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

/**
 * Created by Apple on 16/4/27.
 */
public class WTRetryPolicy implements RetryPolicy {

    /** 默认超时时间 单位 毫秒 */
    public static final int DEFAULT_TIMEOUT_MS = 2500;

    /** 默认重试次数  1次 */
    public static final int DEFAULT_MAX_RETRIES = 1;

    /** 默认乘积倍数 */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    /** 自定义超时时间 */
    private int mCurrentTimeoutMs;

    /** 自定义重试次数 */
    private int mCurrentRetryCount;

    /** 最大尝试次数 */
    private  int mMaxNumRetries;

    /** 自定义乘积倍数 */
    private  float mBackoffMultiplier;
    /**超时回调*/
    private NetworkAccessTimeOut mNetworkAccessTimeOut;


    public WTRetryPolicy(NetworkAccessTimeOut networkAccessTimeOut) {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT, networkAccessTimeOut);
    }

    /**
     * 自定义超时重试方略
     * @param initialTimeoutMs  超时时间
     * @param maxNumRetries     最大重试次数
     * @param backoffMultiplier 乘积倍数
     */
    public WTRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier,NetworkAccessTimeOut networkAccessTimeOut) {

       mNetworkAccessTimeOut=networkAccessTimeOut;
        mCurrentTimeoutMs = initialTimeoutMs;
        mMaxNumRetries = maxNumRetries;
        mBackoffMultiplier = backoffMultiplier;
    }

    @Override
    public int getCurrentTimeout() {
        return mCurrentTimeoutMs;
    }

    @Override
    public int getCurrentRetryCount() {
        return mCurrentRetryCount;
    }
    public float getBackoffMultiplier() {
        return mBackoffMultiplier;
    }

    @Override
    public void retry(VolleyError error) throws VolleyError {
        mCurrentRetryCount++;
        mCurrentTimeoutMs += (mCurrentTimeoutMs * mBackoffMultiplier);
        if (!hasAttemptRemaining()) {
            if (mNetworkAccessTimeOut!=null){
                mNetworkAccessTimeOut.timeOut( mCurrentTimeoutMs*mBackoffMultiplier);
            }
            throw error;

        }

    }

    /**
     * Returns true if this policy has attempts remaining, false otherwise.
     */
    protected boolean hasAttemptRemaining() {
       return mCurrentRetryCount <= mMaxNumRetries;
   // return true;
    }

    public interface NetworkAccessTimeOut{
        void timeOut(float maxTimeOutMs);
    }

    public void setNetworkAccessTimeOut(NetworkAccessTimeOut networkAccessTimeOut) {
        mNetworkAccessTimeOut = networkAccessTimeOut;
    }
}
