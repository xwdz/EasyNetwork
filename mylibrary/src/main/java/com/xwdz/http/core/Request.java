package com.xwdz.http.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class Request implements Serializable {

    private static final long serialVersionUID = -6446819668525759443L;

    public String url;
    public Method method;
    public Object tag;
    public LinkedHashMap<String, String> params;
    public LinkedHashMap<String, String> headers;
    public int connectTimeoutMillis = 15 * 1000;
    public int readTimeoutMillis = 15 * 1000;


    public Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.tag = builder.tag;
        this.params = builder.params;
        this.headers = builder.header;
        this.connectTimeoutMillis = builder.connectTimeoutMillis;
        this.readTimeoutMillis = builder.readTimeoutMills;
    }


    @Override
    public String toString() {
        return "url='" + url + '\'' +
                ", params=" + getPostParams(params);
    }

    public static String getPostParams(HashMap<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }

        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }


    public static class Builder {

        String url;
        Method method;
        Object tag;
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        int connectTimeoutMillis;
        int readTimeoutMills;


        public Builder setConnectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        public Builder setReadTimeoutMills(int readTimeoutMills) {
            this.readTimeoutMills = readTimeoutMills;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            this.method = Method.GET;
            return this;
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder addParam(String key, String value) {
            params.put(key, value);
            return this;
        }

        public Builder addHeader(String key, String value) {
            header.put(key, value);
            return this;
        }

        public Builder addParams(LinkedHashMap<String, String> params) {
            this.params.clear();
            this.params.putAll(params);
            return this;
        }

        public Builder addHeaders(LinkedHashMap<String, String> params) {
            header.clear();
            header.putAll(params);
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }


    public enum Method {
        GET("GET"),
        POST("POST");

        String name;

        Method(String name) {
            this.name = name;
        }
    }
}
