package com.xwdz.http;

import android.text.TextUtils;

import com.xwdz.http.callback.IBaseEasyCallback;
import com.xwdz.http.core.EasyRequestManager;
import com.xwdz.http.core.Request;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class EasyNetwork {

    private static EasyNetwork sEasyNetwork;

    public static EasyNetwork getImpl() {
        if (sEasyNetwork == null) {
            synchronized (EasyNetwork.class) {
                if (sEasyNetwork == null) {
                    sEasyNetwork = new EasyNetwork();
                }
            }
        }
        return sEasyNetwork;
    }


    private EasyRequestManager mEasyRequestManager;

    private EasyConfig mConfig;

    private EasyNetwork() {
        mConfig = new EasyConfig();
        mEasyRequestManager = new EasyRequestManager();
    }

    public void initConfigs(EasyConfig config) {
        this.mConfig = config;
    }


    public void sendRequest(Request request, IBaseEasyCallback baseEasyCallback) {
        if (request == null) {
            throw new NullPointerException("request == null");
        }

        if (TextUtils.isEmpty(request.url)) {
            throw new NullPointerException("url == null");
        }

        request.tag = request.tag == null ? request.url : request.tag;


        mEasyRequestManager.performRequest(mConfig, request, baseEasyCallback);
    }

    public void cancelRequest(Object tag) {
        mEasyRequestManager.performCancelRequest(tag);
    }

    public void cancelAll() {
        mEasyRequestManager.performCancelAll();
    }

}
