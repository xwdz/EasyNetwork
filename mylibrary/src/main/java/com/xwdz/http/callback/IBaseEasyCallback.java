package com.xwdz.http.callback;

import com.xwdz.http.core.EasyCall;

import java.net.HttpURLConnection;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public interface IBaseEasyCallback {

    void onResponse(EasyCall call, HttpURLConnection httpURLConnection);

    void onFailure(EasyCall call, Throwable error);

    void onCancel();
}
