package com.xwdz.http.thread;

import android.os.Handler;
import android.os.Looper;

import com.xwdz.http.Util;
import com.xwdz.http.callback.IBaseEasyCallback;
import com.xwdz.http.core.HttpUrlConnection;
import com.xwdz.http.core.Request;
import com.xwdz.http.error.EasyHTTPException;

import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class RequestTaskWrapper {

    private static final String TAG = RequestTaskWrapper.class.getSimpleName();

    private final AtomicBoolean mCancelled = new AtomicBoolean();
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Request mRequest;
    private IBaseEasyCallback mBaseEasyCallback;
    private FutureTask<HttpURLConnection> mFuture;


    public RequestTaskWrapper(Request request, IBaseEasyCallback baseEasyCallback) {
        this.mRequest = request;
        this.mBaseEasyCallback = baseEasyCallback;
    }

    public Request getRequest() {
        return mRequest;
    }


    public void start() {
        final Callable<HttpURLConnection> callable = new Callable<HttpURLConnection>() {
            @Override
            public HttpURLConnection call() {
                Util.Logger.w(TAG, "ThreadName[" + Thread.currentThread().getName() + "] Running." + mRequest.toString());
                HttpURLConnection result = null;
                try {
                    mTaskInvoked.set(true);
                    result = HttpUrlConnection.execute(mRequest);
                    Util.Logger.w(TAG, "Connection success!");
                } catch (final EasyHTTPException e) {
                    mCancelled.set(true);
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mBaseEasyCallback != null) {
                                mFuture.cancel(false);
                                mBaseEasyCallback.onFailure(e);
                            }
                        }
                    });
                }
                return result;
            }
        };
        mFuture = new FutureTask<HttpURLConnection>(callable) {
            @Override
            protected void done() {
                try {
                    postResultIfNotInvoked(get());
                } catch (Throwable e) {
                    postResultIfNotInvoked(null);
                }
            }

        };

        EasyThreadPools.executor(mFuture);
    }

    private void postResultIfNotInvoked(final HttpURLConnection result) {
        boolean isInvoked = mTaskInvoked.get();
        if (isInvoked) {
            if (mBaseEasyCallback != null) {
                mBaseEasyCallback.onResponse(result);
            }
        }
    }

    private void postMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }


    public void cancel() {

        if (mFuture != null && !mFuture.isCancelled() && !mFuture.isDone()) {
            mFuture.cancel(true);
            Util.Logger.w(TAG, " Cancelled Http Task:" + mRequest.toString());
        }

        if (mBaseEasyCallback != null) {
            mBaseEasyCallback.cancel();
        }
    }

}
