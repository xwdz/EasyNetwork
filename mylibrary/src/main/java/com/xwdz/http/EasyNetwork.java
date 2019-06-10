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


    private static EasyRequestManager sEasyRequestManager = new EasyRequestManager();
    private static EasyNetworkConfig sConfig = new EasyNetworkConfig();



    private EasyNetwork() {

    }

    public static void initializeConfig(EasyNetworkConfig config) {
        sConfig = config;
    }

    public static EasyNetworkConfig getConfig() {
        return sConfig;
    }

    public static void sendRequest(Request request, IBaseEasyCallback baseEasyCallback) {
        if (request == null) {
            throw new NullPointerException("request == null");
        }

        if (TextUtils.isEmpty(request.url)) {
            throw new NullPointerException("url == null");
        }

        request.tag = request.tag == null ? request.url : request.tag;


        sEasyRequestManager.performRequest(sConfig, request, baseEasyCallback);
    }

    public static void cancelRequest(Object tag) {
        sEasyRequestManager.performCancelRequest(tag);
    }

    public static void cancelAll() {
        sEasyRequestManager.performCancelAll();
    }
}
