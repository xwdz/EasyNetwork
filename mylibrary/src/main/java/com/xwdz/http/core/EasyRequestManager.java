package com.xwdz.http.core;

import com.xwdz.http.EasyNetworkConfig;
import com.xwdz.http.callback.IBaseEasyCallback;
import com.xwdz.http.thread.RequestTaskProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class EasyRequestManager {

    private HashMap<String, RequestTaskProxy> mRequestHashMap;

    public EasyRequestManager() {
        mRequestHashMap = new HashMap<>();
    }

    public void performRequest(EasyNetworkConfig config, Request request, IBaseEasyCallback baseEasyCallback) {
        final RequestTaskProxy task = new RequestTaskProxy(config, request, baseEasyCallback);
        task.start();
        mRequestHashMap.put(task.getRequest().tag.toString(), task);

    }


    public void performCancelRequest(Object tag) {
        RequestTaskProxy requestWrapper = mRequestHashMap.get(tag.toString());
        if (requestWrapper != null) {
            requestWrapper.cancel();
        }
    }

    public void performCancelAll() {
        for (Map.Entry<String, RequestTaskProxy> entry : mRequestHashMap.entrySet()) {
            entry.getValue().cancel();
        }
    }
}
