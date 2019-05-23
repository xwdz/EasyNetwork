package com.xwdz.http.core;

import com.xwdz.http.Util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class Request implements Serializable, Cloneable {

    private static final long serialVersionUID = -6446819668525759443L;

    public String url;
    public Method method;
    public Object tag;
    public HashMap<String, String> params;
    public HashMap<String, String> headers;

    private Builder mBuilder;


    public Request(Builder builder) {
        this.mBuilder = builder;
        this.url = builder.url;
        this.method = builder.method;
        this.tag = builder.tag;
        this.params = builder.params;
        this.headers = builder.header;
    }


    public Request newRequest() {
        return new Request(mBuilder);
    }

    @Override
    public Request clone() {
        try {
            return (Request) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newRequest();
    }

    @Override
    public String toString() {
        return "url='" + url + '\'' +
                ", params=" + Util.formatPostParams(params) + ", headers=" + Util.formatPostParams(headers)
                ;
    }

    public static class Builder {

        String url;
        Method method;
        Object tag;
        HashMap<String, String> params = new HashMap<>();
        HashMap<String, String> header = new HashMap<>();
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
            get();
            return this;
        }

        public Builder get() {
            method = Method.GET;
            return this;
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder post() {
            method = Method.POST;
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

        public Builder addParams(HashMap<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder addHeaders(HashMap<String, String> params) {
            header.putAll(params);
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }


    public enum Method {
        GET("get"),
        POST("post");

        String name;

        Method(String name) {
            this.name = name;
        }
    }
}
