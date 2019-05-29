package com.xwdz.http.callback;

import com.xwdz.http.core.EasyCall;
import com.xwdz.http.error.EasyHTTPException;

import java.net.HttpURLConnection;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public abstract class BaseEasyCallbackImpl implements IBaseEasyCallback {

    private static final String TAG = BaseEasyCallbackImpl.class.getSimpleName();

    private volatile boolean isCancelled = false;

    @Override
    public void onResponse(EasyCall call, HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null) {
            onFailure(call, new NullPointerException("HttpURLConnection is null"));
        }
    }

    @Override
    public void onFailure(EasyCall call, Throwable error) {
        //

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
