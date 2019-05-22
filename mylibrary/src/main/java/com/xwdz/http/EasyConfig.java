package com.xwdz.http;

import com.xwdz.http.callback.InterceptRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class EasyConfig {

    private int connectTimeoutMillis = 15 * 1000;
    private int readTimeoutMillis = 15 * 1000;
    private List<InterceptRequest> mInterceptRequests = new ArrayList<>();


    public void addIntercepts(InterceptRequest interceptRequest) {
        this.mInterceptRequests.add(interceptRequest);
    }

    public List<InterceptRequest> getInterceptRequests() {
        return mInterceptRequests;
    }


    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }


    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }
}
