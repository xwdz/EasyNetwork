package com.xwdz.http.core;

import android.webkit.URLUtil;

import com.xwdz.http.Util;
import com.xwdz.http.error.EasyHTTPException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class HttpUrlConnection {

    public static HttpURLConnection execute(Request request) throws EasyHTTPException {
        if (!URLUtil.isNetworkUrl(request.url)) {
            throw new EasyHTTPException(EasyHTTPException.Error.URL_VALID, "the url :" + request.url + " is not valid");
        }
        switch (request.method) {
            case GET:
                return get(request);
            case POST:
                return post(request);
        }

        return null;
    }


    private static HttpURLConnection get(Request request) throws EasyHTTPException {
        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(request.url +
                    Util.appendHttpParams(request.params)).openConnection();
            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(request.connectTimeoutMillis);
            connection.setReadTimeout(request.readTimeoutMillis);

            addHeader(connection, request.headers);

            return connection;
        } catch (Throwable e) {
            throw new EasyHTTPException(EasyHTTPException.Error.SERVICE, e.getMessage());
        }
    }


    private static HttpURLConnection post(Request request) throws EasyHTTPException {
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {

            connection = (HttpURLConnection) new URL(request.url).openConnection();
            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(request.connectTimeoutMillis);
            connection.setReadTimeout(request.readTimeoutMillis);
            connection.setDoOutput(true);


            addHeader(connection, request.headers);
            os = connection.getOutputStream();
            //todo postFile
            os.write(Util.formatPostParams(request.params).getBytes());

        } catch (Throwable e) {
            throw new EasyHTTPException(EasyHTTPException.Error.SERVICE, e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                throw new EasyHTTPException(EasyHTTPException.Error.UN_KNOW, e.getMessage());
            }
        }

        return connection;
    }

    private static void addHeader(HttpURLConnection connection, Map<String, String> headers) {
        if (headers == null || headers.size() == 0)
            return;

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }
}
