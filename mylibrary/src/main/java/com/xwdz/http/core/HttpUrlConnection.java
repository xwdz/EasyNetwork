package com.xwdz.http.core;

import android.webkit.URLUtil;

import com.xwdz.http.EasyNetworkConfig;
import com.xwdz.http.Util;
import com.xwdz.http.callback.InterceptRequest;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class HttpUrlConnection {

    public static HttpURLConnection execute(Request request, EasyNetworkConfig config) throws Throwable {
        if (!URLUtil.isNetworkUrl(request.url)) {
            throw new Throwable("the url :" + request.url + " is not valid.");
        }

        List<InterceptRequest> interceptRequestList = config.getInterceptRequests();
        for (InterceptRequest interceptRequest : interceptRequestList) {
            request = interceptRequest.onInterceptRequest(request);
        }

        switch (request.method) {
            case GET:
                return get(request, config);
            case POST:
                return post(request, config);
        }
        throw new Throwable("please use request method! ");
    }


    private static HttpURLConnection get(Request request, EasyNetworkConfig config) throws Throwable {
        try {
            String realUrl = request.url + Util.appendHttpParams(request.params, true);

            HttpURLConnection connection = (HttpURLConnection) new URL(realUrl).openConnection();
            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(config.getConnectTimeoutMillis());
            connection.setReadTimeout(config.getReadTimeoutMillis());

            addHeader(connection, request.headers);
            connection.setUseCaches(false);
            connection.connect();
            return connection;
        } catch (Throwable e) {
            throw new Throwable(e);
        }
    }


    private static HttpURLConnection post(Request request, EasyNetworkConfig config) throws Throwable {
        OutputStream os = null;
        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(request.url).openConnection();
            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(config.getConnectTimeoutMillis());
            connection.setReadTimeout(config.getReadTimeoutMillis());
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            addHeader(connection, request.headers);
            os = connection.getOutputStream();
            //todo postFile
            os.write(Util.formatPostParams(request.params).getBytes());
            connection.connect();
            return connection;
        } catch (Throwable e) {
            throw new Throwable(e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (Throwable e) {
                throw new Throwable(e);
            }
        }
    }

    private static void addHeader(HttpURLConnection connection, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}
