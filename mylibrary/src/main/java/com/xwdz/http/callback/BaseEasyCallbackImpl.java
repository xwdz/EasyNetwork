package com.xwdz.http.callback;

import com.xwdz.http.error.EasyHTTPException;

import java.net.HttpURLConnection;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public abstract class BaseEasyCallbackImpl implements IBaseEasyCallback {

    private volatile boolean isCancelled = false;


    @Override
    public void onResponse(HttpURLConnection httpURLConnection) {

    }

    @Override
    public void onFailure(Throwable error) {

    }

    @Override
    public void onCancel() {
        isCancelled = true;
    }

    protected void checkIsCancelled() throws EasyHTTPException {
        if (isCancelled) {
            throw new EasyHTTPException("Cancelled Task", EasyHTTPException.Error.CANCEL_REQUEST);
        }
    }
}
