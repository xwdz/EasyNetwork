package com.xwdz.http.thread;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.xwdz.http.EasyNetworkConfig;
import com.xwdz.http.Util;
import com.xwdz.http.callback.IBaseEasyCallback;
import com.xwdz.http.core.HttpUrlConnection;
import com.xwdz.http.core.Request;
import com.xwdz.http.error.EasyHTTPException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class RequestTaskProxy {

    private static final String TAG = RequestTaskProxy.class.getSimpleName();

    private final AtomicBoolean mCancelled = new AtomicBoolean();
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
    private final AtomicBoolean isError = new AtomicBoolean();

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Request mRequest;
    private IBaseEasyCallback mBaseEasyCallback;
    private FutureTask<HttpURLConnection> mFuture;
    private EasyNetworkConfig mConfig;

    private volatile int mRetryCount = 1;


    public RequestTaskProxy(EasyNetworkConfig config, Request request, IBaseEasyCallback baseEasyCallback) {
        this.mRequest = request;
        this.mBaseEasyCallback = baseEasyCallback;
        this.mConfig = config;
    }

    public Request getRequest() {
        return mRequest;
    }


    public void start() {
        final Callable<HttpURLConnection> callable = new Callable<HttpURLConnection>() {
            @Override
            public HttpURLConnection call() {
                Util.Logger.w(TAG, "isRetry [" + (mRetryCount > 1) + "]");
                Util.Logger.w(TAG, "threadName [" + Thread.currentThread().getName() + "] Running.");
                Util.Logger.w(TAG, "request:" + mRequest.toString());
                HttpURLConnection connection = null;
                try {
                    mTaskInvoked.set(true);
                    connection = HttpUrlConnection.execute(mRequest, mConfig);
                    if (connection != null) {
                        final int code = connection.getResponseCode();
                        if (code >= 300 && code < 400) {
                            //重定向
                            if (!TextUtils.isEmpty(connection.getHeaderField("Location"))) {
                                mRequest.url = connection.getHeaderField("Location");
                                Util.Logger.w(TAG, "Redirect to url:" + mRequest.url);
                                start();
                            }
                        }
                    }
                    Util.Logger.w(TAG, "Connection success!");
                } catch (final EasyHTTPException e) {
                    isError.set(true);
                    mCancelled.set(true);

                    //
                    postMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mBaseEasyCallback != null) {
                                mFuture.cancel(false);
                                mBaseEasyCallback.onFailure(e);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    mCancelled.set(true);
                    isError.set(true);
                } finally {
                    if (isError.get() && mConfig.isOpenRetry()) {
                        if (mRetryCount < mConfig.getRetryCount()) {
                            mRetryCount++;
                            Util.Logger.w(TAG, "retry network request [" + mRetryCount + "]");
                            start();
                        }
                    }
                }

                return connection;
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
            mBaseEasyCallback.onCancel();
        }
    }

}
