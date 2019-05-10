package com.xwdz.http.callback;

import android.os.Handler;
import android.os.Looper;

import com.xwdz.http.Util;
import com.xwdz.http.error.EasyHTTPException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public abstract class StringEasyCallbackImpl implements IBaseEasyCallback {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onResponse(final HttpURLConnection httpURLConnection) {
        InputStream is = null;
        ByteArrayOutputStream out = null;
        try {
            final int status = httpURLConnection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                is = httpURLConnection.getInputStream();
                out = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();

                final String result = new String(out.toByteArray());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccessful(result);
                    }
                });
            } else {
                throw new EasyHTTPException(httpURLConnection.getResponseMessage(), String.valueOf(status));
            }


        } catch (final Throwable e) {
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(e);
                }
            });
        } finally {
            Util.quietClose(is);
            Util.quietClose(out);
        }
    }



    @Override
    public void onFailure(Throwable error) {

    }


    public abstract void onSuccessful(String data);

}
