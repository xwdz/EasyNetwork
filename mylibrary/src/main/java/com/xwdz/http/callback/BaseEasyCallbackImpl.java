package com.xwdz.http.callback;

import android.os.Handler;
import android.os.Looper;

import com.xwdz.http.Util;
import com.xwdz.http.core.EasyCall;
import com.xwdz.http.error.EasyHTTPException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public void onResponse(final EasyCall call, final HttpURLConnection httpURLConnection) {
        try {
            final int code = httpURLConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK != code) {
                safeCall(new Runnable() {
                    @Override
                    public void run() {
                        onFailure(call, new Throwable(getErrorMessage(httpURLConnection.getErrorStream())));
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            onFailure(call, new IOException(e));
        }
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

    private String getErrorMessage(InputStream is) {
        ByteArrayOutputStream message = null;
        try {
            int length;
            byte[] buffered = new byte[2048];
            message = new ByteArrayOutputStream();
            while ((length = is.read(buffered)) > 0) {
                message.write(buffered, 0, length);
            }
            return message.toString("UTF-8");
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            Util.quietClose(is);
            Util.quietClose(message);
        }

        return "";
    }
}
