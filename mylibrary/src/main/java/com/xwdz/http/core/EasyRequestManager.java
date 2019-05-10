package com.xwdz.http.core;

import com.xwdz.http.callback.IBaseEasyCallback;
import com.xwdz.http.thread.RequestTaskWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class EasyRequestManager {

    private HashMap<String, RequestTaskWrapper> mRequestHashMap;

    public EasyRequestManager() {
        mRequestHashMap = new HashMap<>();
    }

    public void performRequest(Request request, IBaseEasyCallback baseEasyCallback) {
        final RequestTaskWrapper task = new RequestTaskWrapper(request, baseEasyCallback);
        task.start();
        mRequestHashMap.put(task.getRequest().tag.toString(), task);

    }


    public void performCancelRequest(Object tag) {
        RequestTaskWrapper requestWrapper = mRequestHashMap.get(tag.toString());
        if (requestWrapper != null) {
            requestWrapper.cancel();
        }
    }

    public void performCancelAll() {
        for (Map.Entry<String, RequestTaskWrapper> entry : mRequestHashMap.entrySet()) {
            entry.getValue().cancel();
        }
    }
}
