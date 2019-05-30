package com.xwdz.http;

import android.content.Context;

import com.xwdz.http.callback.InterceptRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class EasyNetworkConfig {


    public static boolean isDebug = true;

    private int connectTimeoutMillis = 15 * 1000;
    private int readTimeoutMillis = 15 * 1000;

    private List<InterceptRequest> interceptRequests = new ArrayList<>();
    // 失败重试次数
    private int retryCount = 3;
    // 是否需要打开重试机制
    private boolean isOpenRetry = true;
    // 每次重试间隔时间
    private long retryIntervalMillis = 2000;

    private Context mContext;


    public EasyNetworkConfig(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void addIntercepts(InterceptRequest interceptRequest) {
        this.interceptRequests.add(interceptRequest);
    }

    public long getRetryIntervalMillis() {
        return retryIntervalMillis;
    }

    public void setRetryIntervalMillis(long retryIntervalMillis) {
        this.retryIntervalMillis = retryIntervalMillis;
    }

    public List<InterceptRequest> getInterceptRequests() {
        return interceptRequests;
    }


    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public boolean isOpenRetry() {
        return isOpenRetry;
    }

    public void setOpenRetry(boolean openRetry) {
        isOpenRetry = openRetry;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

}
