package com.xwdz.http.core;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class EasyCall {

    private Request request;

    public EasyCall() {
    }

    public EasyCall(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

}
