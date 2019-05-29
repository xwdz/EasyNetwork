package com.xwdz.http.callback;

import com.xwdz.http.Util;
import com.xwdz.http.core.EasyCall;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public abstract class StringEasyCallbackImpl extends BaseEasyCallbackImpl {

    @Override
    public void onResponse(final EasyCall call, final HttpURLConnection httpURLConnection) {
        super.onResponse(call, httpURLConnection);
        InputStream is = null;
        ByteArrayOutputStream out = null;
        try {
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

            safeCall(new Runnable() {
                @Override
                public void run() {
                    onSuccessful(result);
                }
            });
        } catch (final Throwable e) {
            e.printStackTrace();
            safeCall(new Runnable() {
                @Override
                public void run() {
                    onFailure(call, e);
                }
            });
        } finally {
            Util.quietClose(is);
            Util.quietClose(out);
            safeCall(new Runnable() {
                @Override
                public void run() {
                    onCompleted();
                }
            });
        }
    }

    public abstract void onSuccessful(String data);

    public abstract void onCompleted();
}
