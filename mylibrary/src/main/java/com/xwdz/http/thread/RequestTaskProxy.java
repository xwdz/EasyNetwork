package com.xwdz.http.thread;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.xwdz.http.EasyNetworkConfig;
import com.xwdz.http.Util;
import com.xwdz.http.callback.IBaseEasyCallback;
import com.xwdz.http.core.EasyCall;
import com.xwdz.http.core.HttpUrlConnection;
import com.xwdz.http.core.Request;

import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class RequestTaskProxy {

    private static final String TAG = RequestTaskProxy.class.getSimpleName();

    private final AtomicBoolean mCancelled = new AtomicBoolean();
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
    private final AtomicBoolean isError = new AtomicBoolean();
    private final AtomicInteger mRetryCount = new AtomicInteger();

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private volatile Request mRequest;
    private volatile IBaseEasyCallback mBaseEasyCallback;
    private EasyNetworkConfig mConfig;
    private FutureTask<HttpURLConnection> mFutureTask;


    private EasyCall mEasyCall;


    public RequestTaskProxy(EasyNetworkConfig config, Request request, IBaseEasyCallback baseEasyCallback) {
        this.mRequest = request;
        this.mBaseEasyCallback = baseEasyCallback;
        this.mConfig = config;
        this.mEasyCall = new EasyCall(mRequest);
    }

    public Request getRequest() {
        return mRequest;
    }


    public void start() {
        final Callable<HttpURLConnection> callable = new Callable<HttpURLConnection>() {
            @Override
            public HttpURLConnection call() {
                Util.Logger.w(TAG, "isRetry: [" + (mRetryCount.get() >= 1) + "]");
                Util.Logger.w(TAG, mRequest.toString());
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
                    mRetryCount.set(0);
                    postResult(connection);
                    Util.Logger.w(TAG, "Connection success!");
                    return connection;
                } catch (final Throwable e) {
                    callError(e);
                } finally {
                    if (isError.get() && mConfig.isOpenRetry()) {
                        if (mRetryCount.getAndIncrement() < mConfig.getRetryCount()) {
                            Util.Logger.w(TAG, "RetryCount:" + mRetryCount.get());
                            try {
                                Thread.sleep(mConfig.getRetryIntervalMillis());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            start();

                            isError.set(false);
                            mCancelled.set(false);
                        }
                    }
                }
                return connection;
            }
        };


        mFutureTask = new FutureTask<HttpURLConnection>(callable) {
            @Override
            protected void done() {
                super.done();
                try {
                    get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    callError(e);
                }
            }
        };
        EasyThreadPools.executor(mFutureTask);
    }

    private void postResult(final HttpURLConnection result) {
        boolean isInvoked = mTaskInvoked.get();
        if (isInvoked) {
            if (mBaseEasyCallback != null) {
                mBaseEasyCallback.onResponse(mEasyCall, result);
            }
        }
    }

    private void callError(final Throwable e) {
        isError.set(true);
        mCancelled.set(true);
        postMainThread(new Runnable() {
            @Override
            public void run() {
                if (mBaseEasyCallback != null) {
                    mBaseEasyCallback.onFailure(mEasyCall, e);
                }
            }
        });
    }


    private void postMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }


    public void cancel() {
        if (mFutureTask != null && !mFutureTask.isCancelled() && !mFutureTask.isDone()) {
            mFutureTask.cancel(true);
            Util.Logger.w(TAG, " Cancelled Http Task:" + mRequest.toString());
        }

        if (mBaseEasyCallback != null) {
            mBaseEasyCallback.onCancel();
        }
    }
}
