package com.xwdz.httpsimple;

import android.util.Log;

import com.xwdz.http.callback.InterceptRequest;
import com.xwdz.http.core.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class AppendGlobalParamsIntercept implements InterceptRequest {
    @Override
    public Request onInterceptRequest(Request request) {
        HashMap<String, String> params = new HashMap<>();
        HashMap<String, String> headers = new HashMap<>();

        headers.put("comm_global_custom_header1", "comm_custom_header1");
        headers.put("comm_global_custom_header2", "comm_custom_header2");

        params.put("comm_global_custom_params1", "comm_custom_params1");
        params.put("comm_global_custom_params2", "comm_custom_params2");

        for (Map.Entry<String, String> old : request.params.entrySet()) {
            params.put(old.getKey(), old.getValue());
        }

        for (Map.Entry<String, String> old : request.headers.entrySet()) {
            headers.put(old.getKey(), old.getValue());
        }

        Request clone = request.clone();
        clone.params.putAll(params);
        clone.headers.putAll(headers);

        Log.w("EasyHttp", "append params intercept:" + clone.toString());
        return clone;
    }
}
