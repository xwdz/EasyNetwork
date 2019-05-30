package com.xwdz.httpsimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xwdz.http.EasyNetwork;
import com.xwdz.http.Util;
import com.xwdz.http.callback.FileEasyCallbackImpl;
import com.xwdz.http.callback.StringEasyCallbackImpl;
import com.xwdz.http.core.EasyCall;
import com.xwdz.http.core.Request;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private TextView mLogView;
    private LinearLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogView = findViewById(R.id.logText);
        mProgressBar = findViewById(R.id.progressBar);


        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                testGET();
            }
        });


        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                testPOST();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        EasyNetwork.getImpl().cancelRequest("TAG");
    }

    public void testGETDownloader() {
        Request request = new Request.Builder()
                .url("http://shouji.360tpcdn.com/150707/2ef5e16e0b8b3135aa714ad9b56b9a3d/com.happyelements.AndroidAnimal_25.apk")
                .tag("TAG")
                .build();

        EasyNetwork.getImpl().sendRequest(request,
                new FileEasyCallbackImpl(
                        Util.getDownloaderDir(this, "easy").getAbsolutePath(),
                        "test.apk") {

                    @Override
                    public void onProgressUpdated(int currentLength, int totalLength, int percent) {
                        mLogView.setText("" + percent + "%");
                    }

                    @Override
                    public void onSuccessful(File file) {
                        mLogView.setText(file.getAbsolutePath());
                    }

                    @Override
                    public void onFailure(EasyCall easyCall, Throwable error) {
                        mLogView.setText(error.toString());
                    }
                });


    }


    public void testGET() {
        Request request = new Request.Builder()
                .url("http://47.106.223.246/test/get")
                .tag("TAG")
                .method(Request.Method.GET)
                .addParam("custom_test_param", "value")
                .addHeader("custom_test_header", "value")
                .build();

        EasyNetwork.getImpl().sendRequest(request, new StringEasyCallbackImpl() {
            @Override
            public void onSuccessful(String data) {
                mLogView.setText(stringToJSON(data));
            }

            @Override
            public void onFailure(EasyCall easyCall, Throwable error) {
                super.onFailure(easyCall, error);
                mLogView.setText(error.toString());
            }

            @Override
            public void onCompleted() {
                mProgressBar.setVisibility(View.GONE);
                mLogView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void testPOST() {
        Request request = new Request.Builder()
                .url("http://47.106.223.246/test/post")
                .post()
                .tag("TAG")
                .addParam("custom_test_param", "value")
                .addHeader("custom_test_header", "value")
                .build();

        EasyNetwork.getImpl().sendRequest(request, new StringEasyCallbackImpl() {
            @Override
            public void onSuccessful(String data) {
                mLogView.setText(stringToJSON(data));
            }

            @Override
            public void onFailure(EasyCall easyCall, Throwable error) {
                super.onFailure(easyCall, error);
                mLogView.setText(error.toString());
            }

            @Override
            public void onCompleted() {
                mProgressBar.setVisibility(View.GONE);
                mLogView.setVisibility(View.VISIBLE);
            }
        });
    }

    public static String stringToJSON(String strJson) {
        // 计数tab的个数
        int tabNum = 0;
        StringBuffer jsonFormat = new StringBuffer();
        int length = strJson.length();

        char last = 0;
        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
                jsonFormat.append(c);
            } else if (c == ',') {
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == ':') {
                jsonFormat.append(c + " ");
            } else if (c == '[') {
                tabNum++;
                char next = strJson.charAt(i + 1);
                if (next == ']') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append(c + "\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                }
            } else if (c == ']') {
                tabNum--;
                if (last == '[') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append("\n" + getSpaceOrTab(tabNum) + c);
                }
            } else {
                jsonFormat.append(c);
            }
            last = c;
        }
        return jsonFormat.toString();
    }

    private static String getSpaceOrTab(int tabNum) {
        StringBuffer sbTab = new StringBuffer();
        for (int i = 0; i < tabNum; i++) {
            sbTab.append('\t');
        }
        return sbTab.toString();
    }
}
