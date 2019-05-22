package com.xwdz.httpsimple;

import android.util.Log;

import com.xwdz.http.callback.InterceptRequest;
import com.xwdz.http.core.Request;

import java.util.HashMap;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class SignatureIntercept implements InterceptRequest {
    @Override
    public Request onInterceptRequest(Request request) {

        HashMap<String, String> params = new HashMap<>();
        HashMap<String, String> headers = new HashMap<>();

        // todo  加密 参数 or header or url
        Request realRequest = new Request.Builder()
                .url(request.url)
                .method(request.method)
                .tag(request.tag)
                .addHeaders(headers)
                .addParams(params)
                .build();

        Log.w("EasyHttp", "signature intercept:" + realRequest.toString());
        return realRequest;
    }
}
