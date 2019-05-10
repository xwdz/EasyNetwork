package com.xwdz.http.callback;

import com.xwdz.http.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public abstract class FileEasyCallbackImpl extends BaseEasyCallbackImpl {

    private String mPath;

    public FileEasyCallbackImpl(String path, String name) {
        mPath = path + name;
    }

    @Override
    public void onResponse(HttpURLConnection connection) {
        FileOutputStream out = null;
        InputStream is = null;

        try {
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                File file = new File(mPath);
                out = new FileOutputStream(file);
                final int totalLen = connection.getContentLength();
                int curLen = 0;
                byte[] buffer = new byte[2048];
                int len;
                int percent = 0;
                while ((len = is.read(buffer)) != -1) {
                    checkIsCancelled();
                    out.write(buffer, 0, len);
                    curLen += len;
                    percent = (curLen * 100 / totalLen);
                    final int finalCurLen = curLen;
                    final int finalPercent = percent;
                    Util.postToMain(new Runnable() {
                        @Override
                        public void run() {
                            onProgressUpdated(finalCurLen, totalLen, finalPercent);
                        }
                    });
                }
                is.close();
                out.flush();
                out.close();

                Util.postToMain(new Runnable() {
                    @Override
                    public void run() {
                        onSuccessful(new File(mPath));
                    }
                });
            }

        } catch (final Throwable e) {
            e.printStackTrace();
            Util.postToMain(new Runnable() {
                @Override
                public void run() {
                    onFailure(e);
                }
            });

        } finally {
            Util.quietClose(out);
            Util.quietClose(is);
        }
    }

    public abstract void onProgressUpdated(int currentLength, int totalLength, int percent);

    public abstract void onSuccessful(File file);


}
