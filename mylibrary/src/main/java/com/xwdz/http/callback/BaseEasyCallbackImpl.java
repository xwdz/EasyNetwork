package com.xwdz.http.callback;

import android.os.Handler;
import android.os.Looper;

import com.xwdz.http.core.EasyCall;
import com.xwdz.http.error.EasyHTTPException;

import java.net.HttpURLConnection;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public abstract class BaseEasyCallbackImpl implements IBaseEasyCallback {

    private static final String TAG = BaseEasyCallbackImpl.class.getSimpleName();


    protected Handler mHandler = new Handler(Looper.getMainLooper());

    private volatile boolean isCancelled = false;

    @Override
    public void onResponse(final EasyCall call, HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null) {
            safeCall(new Runnable() {
                @Override
                public void run() {
                    onFailure(call, new NullPointerException("HttpURLConnection is null"));
                }
            });
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
    protected void safeCall(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }
}
