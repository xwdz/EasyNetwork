package com.xwdz.http.callback;

import java.net.HttpURLConnection;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public interface IBaseEasyCallback {

    void onResponse(HttpURLConnection httpURLConnection);

    void onFailure(Throwable error);

    void cancel();

}
